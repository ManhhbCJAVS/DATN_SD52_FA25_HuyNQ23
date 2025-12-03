package edu.poly.datn_sd52_fa25_huynq203.library.config.security;

import edu.poly.datn_sd52_fa25_huynq203.library.exception.handler.SecurityExceptionHandler;
import edu.poly.datn_sd52_fa25_huynq203.library.service.user.CustomerService;
import edu.poly.datn_sd52_fa25_huynq203.library.service.user.EmployeeService;
import lombok.RequiredArgsConstructor;

import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SecurityConfig {

    // 2. Thêm final thủ công cho các Bean cần Inject qua Constructor
    final JwtAuthenticationFilter jwtAuthFilter;
    final EmployeeService employeeService;
    final CustomerService customerService;
    final SecurityExceptionHandler securityExceptionHandler;

    // 3. Inject giá trị từ properties bằng @Value
    @Value("${backoffice.endpoint}")
    String backofficeEndpoint; // Giá trị: /backoffice

    @Value("${client.endpoint}")
    String clientEndpoint; // Giá trị: /store

    // Danh sách các đường dẫn của Swagger cần mở
    String[] SWAGGER_WHITELIST = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/swagger-resources/**",
            "/webjars/**",
            "/configuration/ui",
            "/configuration/security"
    };

    @Bean
    public SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(securityExceptionHandler) // Dùng chung bean
                        .accessDeniedHandler(securityExceptionHandler)      // Dùng chung bean
                )
                // Thêm JWT Filter để check token cho mọi request
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        // --- 1. SWAGGER (Public) ---
                        .requestMatchers(SWAGGER_WHITELIST).permitAll()

                        // --- 2. AUTHENTICATION (Public) ---
                        .requestMatchers(backofficeEndpoint + "/login").permitAll()
                        .requestMatchers(backofficeEndpoint + "/register").permitAll()
                        .requestMatchers(backofficeEndpoint + "/forgot-password").permitAll()

                        .requestMatchers(clientEndpoint + "/**").permitAll()

                        // 3. PROTECTED BACKOFFICE ENDPOINTS
                        .requestMatchers(backofficeEndpoint + "/**").hasAnyRole("ADMIN", "STAFF")

                        // TODO: 1so cua client can xac thực.
                        .anyRequest().authenticated()); // require authentication
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
        //return new BCryptPasswordEncoder(); Sau mã hóa dùng cái này.
        // Sử dụng NoOpPasswordEncoder để so sánh mật khẩu thô
        return NoOpPasswordEncoder.getInstance();
    }
}