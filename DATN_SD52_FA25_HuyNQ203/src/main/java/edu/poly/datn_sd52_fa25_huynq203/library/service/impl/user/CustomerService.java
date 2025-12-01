package edu.poly.datn_sd52_fa25_huynq203.library.service.impl.user;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.user.Customer;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.user.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerService implements UserDetailsService {

    private final CustomerRepository customerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer user = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Customer not found with email: " + email));

        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .roles("CUSTOMER")
                .disabled(employee.getStatus() == EmployeeStatus.INACTIVE)
                // Có thể set accountLocked, accountExpired, credentialsExpired nếu có dữ liệu tương ứng
                .build();
    }
}
