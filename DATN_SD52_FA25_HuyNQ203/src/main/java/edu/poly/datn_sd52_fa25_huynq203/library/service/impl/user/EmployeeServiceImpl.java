package edu.poly.datn_sd52_fa25_huynq203.library.service.impl.user;

import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.DuplicateFieldException;
import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.ResourceNotFoundException;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.attribute.EmployeeRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.user.EmployeeResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.user.Employee;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.EmployeeStatus;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.Role;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.user.EmployeeRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.service.user.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public EmployeeResponse createEmployee(EmployeeRequest request) {
        // Validate duplicate
        if (request.getEmail() != null && employeeRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateFieldException("email", request.getEmail());
        }
        if (request.getPhone() != null && employeeRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateFieldException("phone", request.getPhone());
        }

        // Generate code
        String code = generateEmployeeCode();

        // Create entity
        Employee employee = Employee.builder()
                .code(code)
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole() != null ? request.getRole() : Role.STAFF)
                .avatar(request.getAvatar())
                .birthday(request.getBirthday())
                .gender(request.getGender())
                .note(request.getNote())
                .status(request.getStatus() != null ? request.getStatus() : EmployeeStatus.ACTIVE)
                .createdAt(LocalDate.now())
                .build();

        Employee savedEmployee = employeeRepository.save(employee);
        return mapToResponse(savedEmployee);
    }

    @Override
    @Transactional
    public EmployeeResponse updateEmployee(Long id, EmployeeRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + id));

        // Validate duplicate (exclude current employee)
        if (request.getEmail() != null && !request.getEmail().equals(employee.getEmail())) {
            if (employeeRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateFieldException("email", request.getEmail());
            }
        }
        if (request.getPhone() != null && !request.getPhone().equals(employee.getPhone())) {
            if (employeeRepository.existsByPhone(request.getPhone())) {
                throw new DuplicateFieldException("phone", request.getPhone());
            }
        }

        // Update fields
        employee.setName(request.getName());
        employee.setPhone(request.getPhone());
        employee.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            employee.setPassword(request.getPassword());
        }
        if (request.getRole() != null) {
            employee.setRole(request.getRole());
        }
        employee.setAvatar(request.getAvatar());
        employee.setBirthday(request.getBirthday());
        employee.setGender(request.getGender());
        employee.setNote(request.getNote());
        if (request.getStatus() != null) {
            employee.setStatus(request.getStatus());
        }

        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToResponse(updatedEmployee);
    }

    @Override
    public EmployeeResponse getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + id));
        return mapToResponse(employee);
    }

    @Override
    @Transactional
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + id);
        }
        employeeRepository.deleteById(id);
    }

    @Override
    public Page<EmployeeResponse> searchEmployees(String keyword, EmployeeStatus status, Role role, Pageable pageable) {
        Page<Employee> employees = employeeRepository.searchEmployees(keyword, status, role, pageable);
        return employees.map(this::mapToResponse);
    }

    @Override
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeResponse changeStatus(Long id, EmployeeStatus status) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + id));
        employee.setStatus(status);
        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToResponse(updatedEmployee);
    }

    @Override
    public EmployeeResponse login(String email, String password) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email hoặc mật khẩu không đúng"));

        if (!employee.getPassword().equals(password)) {
            throw new ResourceNotFoundException("Email hoặc mật khẩu không đúng");
        }

        if (employee.getStatus() != EmployeeStatus.ACTIVE) {
            throw new ResourceNotFoundException("Tài khoản đã bị khóa");
        }

        return mapToResponse(employee);
    }

    private String generateEmployeeCode() {
        long count = employeeRepository.count();
        return String.format("NV%06d", count + 1);
    }

    private EmployeeResponse mapToResponse(Employee employee) {
        return EmployeeResponse.builder()
                .id(employee.getId())
                .code(employee.getCode())
                .name(employee.getName())
                .phone(employee.getPhone())
                .email(employee.getEmail())
                .role(employee.getRole())
                .avatar(employee.getAvatar())
                .birthday(employee.getBirthday())
                .gender(employee.getGender())
                .note(employee.getNote())
                .status(employee.getStatus())
                .createdAt(employee.getCreatedAt())
                .build();
    }
}

