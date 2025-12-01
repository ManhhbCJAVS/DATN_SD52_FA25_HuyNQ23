package edu.poly.datn_sd52_fa25_huynq203.library.config.security;

import edu.poly.datn_sd52_fa25_huynq203.library.service.impl.user.CustomerService;
import edu.poly.datn_sd52_fa25_huynq203.library.service.impl.user.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SecurityConfig {

    JWTAuthFilter jwtAuthFilter;
    EmployeeService employeeService;
    CustomerService customerService;

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .csrf(CsrfConfigurer::disable)
//                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
//                .authenticationProvider(authenticationProvider())
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .authorizeHttpRequests(request -> request
//                        .requestMatchers("/api/auth/**").permitAll()   // login / register / public auth
//                        .requestMatchers("/admin/**").hasRole("ADMIN")
//                        .requestMatchers("/api/user/**").hasAnyRole("USER", "ADMIN")
//                        .anyRequest().authenticated() // các request khác yêu cầu login
//                );
//        return httpSecurity.build();
//    }

    @Bean
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))

                // Thêm JWT Filter để check token cho mọi request
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // API Public (Login, Register...)
                        .requestMatchers("/api/auth/**", "/login", "/register").permitAll()

                        // API dành riêng cho Admin
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // API dành cho User (Customer)
                        .requestMatchers("/api/user/**").hasAnyRole("CUSTOMER", "ADMIN")

                        // Còn lại bắt buộc phải xác thực
                        .anyRequest().authenticated());
        return http.build();
    }

    // ==========================================
    // 2. KHAI BÁO CÁC PROVIDER (CUNG CẤP CHO CONTROLLER)
    // ==========================================

    @Bean()
    public AuthenticationProvider adminAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(employeeService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean()
    public AuthenticationProvider userAuthProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(customerService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}