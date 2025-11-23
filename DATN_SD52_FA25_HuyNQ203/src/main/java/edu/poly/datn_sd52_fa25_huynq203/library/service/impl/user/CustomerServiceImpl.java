package edu.poly.datn_sd52_fa25_huynq203.library.service.impl.user;

import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.DuplicateFieldException;
import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.ResourceNotFoundException;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.product.CustomerRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.user.CustomerResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.user.Customer;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.CustomerStatus;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.user.CustomerRepository;
import edu.poly.datn_sd52_fa25_huynq203.library.service.user.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        // Validate duplicate
        if (request.getEmail() != null && customerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateFieldException("email", request.getEmail());
        }
        if (request.getPhone() != null && customerRepository.existsByPhone(request.getPhone())) {
            throw new DuplicateFieldException("phone", request.getPhone());
        }

        // Generate code
        String code = generateCustomerCode();

        // Create entity
        Customer customer = Customer.builder()
                .code(code)
                .name(request.getName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .password(request.getPassword())
                .avatar(request.getAvatar())
                .birthday(request.getBirthday())
                .gender(request.getGender())
                .status(request.getStatus() != null ? request.getStatus() : CustomerStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        Customer savedCustomer = customerRepository.save(customer);
        return mapToResponse(savedCustomer);
    }

    @Override
    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + id));

        // Validate duplicate (exclude current customer)
        if (request.getEmail() != null && !request.getEmail().equals(customer.getEmail())) {
            if (customerRepository.existsByEmail(request.getEmail())) {
                throw new DuplicateFieldException("email", request.getEmail());
            }
        }
        if (request.getPhone() != null && !request.getPhone().equals(customer.getPhone())) {
            if (customerRepository.existsByPhone(request.getPhone())) {
                throw new DuplicateFieldException("phone", request.getPhone());
            }
        }

        // Update fields
        customer.setName(request.getName());
        customer.setPhone(request.getPhone());
        customer.setEmail(request.getEmail());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            customer.setPassword(request.getPassword());
        }
        customer.setAvatar(request.getAvatar());
        customer.setBirthday(request.getBirthday());
        customer.setGender(request.getGender());
        if (request.getStatus() != null) {
            customer.setStatus(request.getStatus());
        }
        customer.setUpdatedAt(LocalDateTime.now());

        Customer updatedCustomer = customerRepository.save(customer);
        return mapToResponse(updatedCustomer);
    }

    @Override
    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + id));
        return mapToResponse(customer);
    }

    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + id);
        }
        customerRepository.deleteById(id);
    }

    @Override
    public Page<CustomerResponse> searchCustomers(String keyword, CustomerStatus status, Pageable pageable) {
        Page<Customer> customers = customerRepository.searchCustomers(keyword, status, pageable);
        return customers.map(this::mapToResponse);
    }

    @Override
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CustomerResponse changeStatus(Long id, CustomerStatus status) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + id));
        customer.setStatus(status);
        customer.setUpdatedAt(LocalDateTime.now());
        Customer updatedCustomer = customerRepository.save(customer);
        return mapToResponse(updatedCustomer);
    }

    private String generateCustomerCode() {
        long count = customerRepository.count();
        return String.format("KH%06d", count + 1);
    }

    private CustomerResponse mapToResponse(Customer customer) {
        return CustomerResponse.builder()
                .id(customer.getId())
                .code(customer.getCode())
                .name(customer.getName())
                .phone(customer.getPhone())
                .email(customer.getEmail())
                .avatar(customer.getAvatar())
                .birthday(customer.getBirthday())
                .gender(customer.getGender())
                .status(customer.getStatus())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .createdByName(customer.getCreatedBy() != null ? customer.getCreatedBy().getName() : null)
                .updatedByName(customer.getUpdatedBy() != null ? customer.getUpdatedBy().getName() : null)
                .build();
    }
}

