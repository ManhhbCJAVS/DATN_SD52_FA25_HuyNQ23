package edu.poly.datn_sd52_fa25_huynq203.library.config.security;

import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.BusinessException;
import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.ExceptionType;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.TokenType;
import edu.poly.datn_sd52_fa25_huynq203.library.service.jwt.JwtService;
import edu.poly.datn_sd52_fa25_huynq203.library.service.user.CustomerService;
import edu.poly.datn_sd52_fa25_huynq203.library.service.user.EmployeeService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Log4j2
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Qualifier("handlerExceptionResolver")
    final HandlerExceptionResolver resolver;
    final JwtService jwtService;
    final EmployeeService employeeService;
    final CustomerService customerService;

    @Value("${backoffice.endpoint}")
    String backofficeEndpoint;

    @Value("${client.endpoint}")
    String clientEndpoint;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // [QUAN TRỌNG] Logic chọn tên Cookie dựa trên Endpoint
        // Nếu vào /backoffice -> tìm cookie "staff_accessToken"
        // Nếu vào /store -> tìm cookie "client_accessToken" (hoặc tên bạn quy định cho client)
        String requestPath = request.getServletPath();
        String cookieName = requestPath.startsWith(backofficeEndpoint) ? "staff_accessToken" : "client_accessToken";
        // [FIX 1] getCookieValue không được ném Exception nếu null, vì trang Login chưa có cookie
        String token = getCookieValue(request, cookieName);
        log.info("Token: {}", token);
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 1. Parse Token (Check Signature + Expiration)
            Claims claims = jwtService.extractAllClaims(token, TokenType.ACCESS_TOKEN);
            String userEmail = claims.getSubject(); // Throw Exception if email = null
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 3. Load UserDetails tùy theo luồng (Admin -> EmployeeService, User -> CustomerService)
                UserDetails userDetails = requestPath.startsWith(backofficeEndpoint) // Throw Exception if user not found.
                        ? employeeService.loadUserByUsername(userEmail)
                        : customerService.loadUserByUsername(userEmail);
                // User isActive?
                if (!userDetails.isEnabled()) throw new BusinessException(ExceptionType.ACCOUNT_LOCKED);

                log.info("UserDetails Info => Username: {}, Password: {}, Authorities: {}, Enabled: {}",
                        userDetails.getUsername(),
                        userDetails.getPassword(),
                        userDetails.getAuthorities(),
                        userDetails.isEnabled()
                );

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken
                        (userDetails, null, userDetails.getAuthorities());

                SecurityContext context = SecurityContextHolder.createEmptyContext();
                context.setAuthentication(authToken);
                SecurityContextHolder.setContext(context);
            }
            filterChain.doFilter(request, response); // Token hợp lệ -> Đi tiếp
        } catch (Exception e) {
            // Log lỗi kiểm thử
            log.error("Authentication Exception: {}", e.getMessage());
            // Delegate cho GlobalExceptionHandler xử lý để trả về JSON ErrorResponse chuẩn
            resolver.resolveException(request, response, null, e);
        }
    }

    private String getCookieValue(@NotNull HttpServletRequest request, @NotNull String cookieName) {
        return Arrays.stream(Optional.ofNullable(request.getCookies()).orElse(new Cookie[0]))
                .filter(c -> cookieName.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null); // [FIX 1]
    }
}
