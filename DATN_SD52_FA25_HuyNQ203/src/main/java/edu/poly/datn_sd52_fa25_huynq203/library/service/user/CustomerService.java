package edu.poly.datn_sd52_fa25_huynq203.library.service.user;

import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.BusinessException;
import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.ExceptionType;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.user.CustomerRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.user.CustomerResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.user.Customer;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.CustomerStatus;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.user.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer user = customerRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ExceptionType.UNAUTHORIZED, "Client not found with email : " + email));

        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("CUSTOMER")
                .disabled(user.getStatus() == CustomerStatus.INACTIVE)
                // Có thể set accountLocked, accountExpired, credentialsExpired nếu có dữ liệu tương ứng
                .build();
    }


    @Transactional
    public CustomerResponse createCustomer(CustomerRequest request) {
        // Validate duplicate
        if (request.getEmail() != null && customerRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ExceptionType.DUPLICATE_FIELD_VALUE, "email đã tồn tại: " + request.getEmail());
        }
        if (request.getPhone() != null && customerRepository.existsByPhone(request.getPhone())) {
            throw new BusinessException(ExceptionType.DUPLICATE_FIELD_VALUE, "số điện thoại đã tồn tại: " + request.getPhone());
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


    @Transactional
    public CustomerResponse updateCustomer(Long id, CustomerRequest request) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionType.RESOURCE_NOT_FOUND, "Không tìm thấy khách hàng với ID: " + id));

        // Validate duplicate (exclude current customer)
        if (request.getEmail() != null && !request.getEmail().equals(customer.getEmail())) {
            if (customerRepository.existsByEmail(request.getEmail())) {
                throw new BusinessException(ExceptionType.DUPLICATE_FIELD_VALUE, "email đã tồn tại: " + request.getEmail());
            }
        }
        if (request.getPhone() != null && !request.getPhone().equals(customer.getPhone())) {
            if (customerRepository.existsByPhone(request.getPhone())) {
                throw new BusinessException(ExceptionType.DUPLICATE_FIELD_VALUE, "số điện thoại đã tồn tại: " + request.getPhone());
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


    public CustomerResponse getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionType.RESOURCE_NOT_FOUND, "Không tìm thấy khách hàng với ID: " + id));
        return mapToResponse(customer);
    }


    @Transactional
    public void deleteCustomer(Long id) {
        if (!customerRepository.existsById(id)) {
            throw new BusinessException(ExceptionType.RESOURCE_NOT_FOUND, "Không tìm thấy khách hàng với ID: " + id);
        }
        customerRepository.deleteById(id);
    }


    public Page<CustomerResponse> searchCustomers(String keyword, CustomerStatus status, Pageable pageable) {
        Page<Customer> customers = customerRepository.searchCustomers(keyword, status, pageable);
        return customers.map(this::mapToResponse);
    }


    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }


    @Transactional
    public CustomerResponse changeStatus(Long id, CustomerStatus status) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ExceptionType.RESOURCE_NOT_FOUND, "Không tìm thấy khách hàng với ID: " + id));
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

