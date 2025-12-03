package edu.poly.datn_sd52_fa25_huynq203.library.exception.base;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

/**
 * @Param HTTP Status (status):
 * - Là mã trạng thái của HTTP (HTTP status code).
 * - Được định nghĩa trong chuẩn HTTP, do server trả về để client biết request thành công hay thất bại và lỗi thuộc loại nào.
 * - Gồm:
 * + 1xx (Thông Báo): request đã nhận & đang xử lý.
 * + 2xx (Thành Công): xử lý thành công.
 * + 3xx (Điều Chuyển): Client cần thực hiện 1 y.cầu khác để hoàn thành yêu câu ban đầu.
 * + 4xx (Lỗi Khách): Lỗi Client, thg do req k hợp lệ | thiếu thông tin cần thiết.
 * + 5xx (Lỗi Server): Lỗi Server khi xử lý request.
 * @Param Application Code (code):
 * - Là mã lỗi riêng của ứng dụng, không phải HTTP.
 * - Ví dụ: INVALID_REQUEST_BODY, USER_NOT_FOUND, TOKEN_EXPIRED
 * - Giúp client hoặc frontend phân biệt lỗi nghiệp vụ cụ thể.
 */

@AllArgsConstructor
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@JsonFormat(shape = JsonFormat.Shape.OBJECT) // <-- QUAN TRỌNG: Dòng này giúp Enum hiển thị full object
public enum ExceptionType {

    // =========================================================================
    // GROUP 1: AUTHENTICATION & AUTHORIZATION (Xác thực & Phân quyền)
    // =========================================================================

    UNAUTHORIZED("UNAUTHORIZED", HttpStatus.UNAUTHORIZED.value(),
            "Bạn cần xác thực để truy cập tài nguyên này. Vui lòng kiểm tra token."),

    INVALID_TOKEN("INVALID_TOKEN", HttpStatus.UNAUTHORIZED.value(),
            "Token không hợp lệ, sai chữ ký hoặc không đúng định dạng."),

    // Nếu muốn tách riêng lỗi hết hạn (Recommended cho Frontend dễ xử lý Refresh Token):
    TOKEN_EXPIRED("TOKEN_EXPIRED", HttpStatus.UNAUTHORIZED.value(),
            "Token đã hết hạn. Vui lòng đăng nhập lại hoặc làm mới token."),

    INVALID_CREDENTIALS("INVALID_CREDENTIALS", HttpStatus.UNAUTHORIZED.value(),
            "Email hoặc mật khẩu không đúng. Vui lòng kiểm tra lại thông tin đăng nhập."),

    ACCOUNT_LOCKED("ACCOUNT_LOCKED", HttpStatus.UNAUTHORIZED.value(),
            "Tài khoản đã bị khóa. Vui lòng liên hệ quản trị viên để mở khóa."),

    FORBIDDEN("FORBIDDEN", HttpStatus.FORBIDDEN.value(),
            "Bạn không có quyền truy cập vào tài nguyên này."),

    // =========================================================================
    // GROUP 2: INPUT VALIDATION & DATA INTEGRITY (Dữ liệu đầu vào)
    // =========================================================================

    INVALID_REQUEST_BODY("INVALID_REQUEST_BODY", HttpStatus.BAD_REQUEST.value(),
            "Yêu cầu không hợp lệ. Dữ liệu đầu vào sai định dạng hoặc thiếu trường bắt buộc."),

    INVALID_JSON_FORMAT("INVALID_JSON_FORMAT", HttpStatus.BAD_REQUEST.value(),
            "Định dạng JSON không hợp lệ. Vui lòng kiểm tra cú pháp và kiểu dữ liệu."),

    DUPLICATE_FIELD_VALUE("DUPLICATE_FIELD_VALUE", HttpStatus.BAD_REQUEST.value(),
            "Dữ liệu bị trùng lặp (ví dụ: code, email, phone)."),

    // =========================================================================
    // GROUP 3: STANDARD HTTP RESOURCES (Tài nguyên & Giao thức)
    // =========================================================================
    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND.value(),
            "Tài nguyên không tìm thấy."),

    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", HttpStatus.METHOD_NOT_ALLOWED.value(),
            "Phương thức HTTP không được phép cho tài nguyên này."),

    UNSUPPORTED_MEDIA_TYPE("UNSUPPORTED_MEDIA_TYPE", HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
            "Định dạng Media Type không được hỗ trợ. Vui lòng sử dụng application/json."),

    // =========================================================================
    // GROUP 4: SERVER & SYSTEM ERRORS (Lỗi hệ thống & Bên thứ 3)
    // =========================================================================
    CloudinaryOperationException("CLOUDINARY_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Lỗi đọc file hoặc kết nối Cloudinary."),

    CLOUDINARY_ERROR("CLOUDINARY_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Lỗi xảy ra trong quá trình xử lý lưu trữ hình ảnh (Cloudinary)."),

    GENERAL_ERROR("GENERAL_ERROR", HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Đã xảy ra lỗi hệ thống không mong muốn. Vui lòng liên hệ hỗ trợ.");

    String code;      // Mã lỗi ứng dụng (business code)
    int status;       // HTTP status code (int)
    String defaultMessage; // Thông điệp mặc định
}
