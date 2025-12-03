package edu.poly.datn_sd52_fa25_huynq203.library.exception.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL) // Ẩn các trường null khỏi JSON
public class ErrorResponse {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime timestamp; //"2025-12-02 02:23:03",
    int status; // 400
    String code; // "RESOURCE_NOT_FOUND"
    String messageExceptionType; // "Lỗi logic nghiệp vụ." | "Dữ liệu đầu vào không hợp lệ." | "Thiếu tham số bắt buộc." | "Tham số không đúng định dạng." | "Lỗi cú pháp JSON." | "JSON chứa trường không hợp lệ." | "Đã xảy ra lỗi hệ thống không mong muốn. Vui lòng liên hệ hỗ trợ."
    String path;  // "admin/xxx"

    String  businessError; // "Product not found with ID: 1234"
    Map<String, String> validationErrors; // Dành cho các Exception liên quan dến: Validation
}
