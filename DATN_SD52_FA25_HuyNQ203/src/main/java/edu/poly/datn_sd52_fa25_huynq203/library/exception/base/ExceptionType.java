package edu.poly.datn_sd52_fa25_huynq203.library.exception.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ExceptionType {

    // =========================================================================
    // 4XX CLIENT ERRORS (Lỗi do người dùng)
    // =========================================================================

    // 400 - Bad Request (Các lỗi liên quan đến dữ liệu gửi lên)
    INVALID_REQUEST_BODY("INVALID_REQUEST_BODY", HttpStatus.BAD_REQUEST, "Yêu cầu không hợp lệ. Dữ liệu đầu vào sai định dạng hoặc thiếu trường bắt buộc."),
    DUPLICATE_FIELD_VALUE("DUPLICATE_FIELD_VALUE", HttpStatus.BAD_REQUEST, "Dữ liệu bị trùng lặp (ví dụ: code, email, phone)."),
    //Lỗi trong JSON Request Body
    INVALID_JSON_FORMAT("INVALID_JSON_FORMAT", HttpStatus.BAD_REQUEST, "Định dạng JSON không hợp lệ. Vui lòng kiểm tra cú pháp và kiểu dữ liệu."),

    // 401 - Unauthorized (Lỗi chưa xác thực - Cần đăng nhập/Token)
    UNAUTHORIZED("UNAUTHORIZED", HttpStatus.UNAUTHORIZED, "Bạn cần xác thực để truy cập tài nguyên này. Vui lòng kiểm tra token."),

    // 403 - Forbidden (Lỗi cấm truy cập - Đã đăng nhập nhưng không có quyền)
    FORBIDDEN("FORBIDDEN", HttpStatus.FORBIDDEN, "Bạn không có quyền truy cập vào tài nguyên này."),

    // 404 - Not Found
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND",  HttpStatus.NOT_FOUND, "Tài nguyên không tìm thấy."),

    // 405 - Method Not Allowed
    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", HttpStatus.METHOD_NOT_ALLOWED, "Phương thức HTTP không được phép cho tài nguyên này."),

    // 415 - Unsupported Media Type
    UNSUPPORTED_MEDIA_TYPE("UNSUPPORTED_MEDIA_TYPE", HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Định dạng Media Type không được hỗ trợ. Vui lòng sử dụng application/json."),

    // =========================================================================
    // 5XX SERVER ERRORS (Lỗi hệ thống)
    // =========================================================================
    CloudinaryOperationException("CLOUDINARY_ERROR" , HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi đọc file hoặc kết nối Cloudinary."),
    CLOUDINARY_ERROR("CLOUDINARY_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi xảy ra trong quá trình xử lý Cloudinary."),
    GENERAL_ERROR("GENERAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi hệ thống không mong muốn.");
    String code;
    HttpStatus httpStatus; // value: 404 & reasonPhrase: Not Found
    String defaultMessage;

}
