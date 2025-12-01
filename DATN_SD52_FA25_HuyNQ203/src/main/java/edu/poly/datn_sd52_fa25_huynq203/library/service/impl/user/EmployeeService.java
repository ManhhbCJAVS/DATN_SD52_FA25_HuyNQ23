package edu.poly.datn_sd52_fa25_huynq203.library.service.impl.user;

import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.user.Employee;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.EmployeeStatus;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.user.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService implements UserDetailsService {

    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Employee not found with email:  " + email));

        return org.springframework.security.core.userdetails.User.withUsername(employee.getEmail())
                .password(employee.getPassword())
                .roles(employee.getRole().name())
                .disabled(employee.getStatus() == EmployeeStatus.INACTIVE)
                // Có thể set accountLocked, accountExpired, credentialsExpired nếu có dữ liệu tương ứng
                .build();
    }
}
