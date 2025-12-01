package edu.poly.datn_sd52_fa25_huynq203.library.config.security;

import edu.poly.datn_sd52_fa25_huynq203.library.service.impl.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class JWTAuthFilter extends OncePerRequestFilter {

    JwtService jwtService;
    UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Gọi tới đây nếu login thành công và đã có JWT.

        // Extract JWT lấy thông tin để xác thực trong CSDL xem user tồn tại k?
        // Dựa vào role mà xem xác thực ở CSDL nào? Admin/Employee | Customer
    }
}
