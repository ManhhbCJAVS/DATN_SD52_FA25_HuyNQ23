package edu.poly.datn_sd52_fa25_huynq203.library.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    // 1. Xử lý lỗi 401 (Chưa đăng nhập / Token sai) - Override từ AuthenticationEntryPoint
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {
        writeResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "UNAUTHORIZED", "Bạn chưa đăng nhập hoặc Token không hợp lệ.");
    }

    // 2. Xử lý lỗi 403 (Không đủ quyền) - Override từ AccessDeniedHandler
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException)
            throws IOException, ServletException {
        writeResponse(response, HttpServletResponse.SC_FORBIDDEN, "ACCESS_DENIED_FILTER", "Bạn không có quyền truy cập tài nguyên này.");
    }

    // Hàm dùng chung để viết JSON ra response (DRY - Don't Repeat Yourself)
    private void writeResponse(HttpServletResponse response, int status, String code, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8"); // Đảm bảo tiếng Việt không bị lỗi font

        // Tạo Map dữ liệu trả về giống với GlobalExceptionHandler của bạn
        Map<String, Object> body = new HashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status);
        body.put("code", code);
        body.put("messageExceptionType", message);
        // Có thể thêm path nếu muốn: body.put("path", ...);

        // Ghi ra response
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), body);
    }
}
