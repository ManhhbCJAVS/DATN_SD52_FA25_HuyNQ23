package edu.poly.datn_sd52_fa25_huynq203.library.exception.handler;

import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.BusinessException;
import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ==================================================================================================
    // 1. HANDLER CHO CÁC LỖI NGHIỆP VỤ TÙY CHỈNH (Custom Business Exceptions)
    // ==================================================================================================
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, WebRequest request) {
        logger.warn("BusinessException handled: {}", ex.getMessage());
        ErrorResponse body = baseError(request, "Lỗi logic nghiệp vụ.");
        body.setExType(ex.getExType());
        return ResponseEntity.status(ex.getExType().getHttpStatus()).body(body);
    }

    // =========================================================================
    // 2. HANDLER LỖI VALIDATION VÀ REQUEST (Input Validation & Client Errors)
    // =========================================================================

    /**
     * Xảy ra: DTO trong @RequestBody có @Valid và JSON gửi lên sai hoặc thiếu dữ liệu theo ràng buộc.
     * (trong BindingResult khi Spring bind JSON → Object)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        logger.warn("handleMethodArgumentNotValid handled: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        ErrorResponse body = baseError(request, "Dữ liệu đầu vào không hợp lệ.");
        body.setValidationErrors(errors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * Xảy ra: validation áp vào tham số phương thức (method parameter) chứ không phải DTO.
     * Các trường hợp bao gồm:
     * 📌 1. @RequestParam / @PathVariable có annotation validation
     *
     * @Min, @Max, @NotBlank, @Pattern,…
     * 📌 2. Object KHÔNG phải @RequestBody nhưng có @Valid
     * Ví dụ lấy dữ liệu từ query nhưng là kiểu Object
     * 📌 3. Validation trong Service khi class có @Validated
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        logger.warn("ConstraintViolationException handled: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(v -> {
            String path = v.getPropertyPath().toString();
            String field = path.substring(path.lastIndexOf('.') + 1);
            errors.put(field, v.getMessage());
        });

        ErrorResponse body = baseError(request, "Lỗi ràng buộc tham số đầu vào.");
        body.setValidationErrors(errors);

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Xảy ra: Thiếu tham số bắt buộc trong URL/Query (MissingServletRequestParameterException).
     * Thường xảy ra khi:
     * 📌 @RequestParam (required = true) : nhưng FE không truyền.
     * 📌 Chỉ áp dụng cho @RequestParam
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, WebRequest request) {
        logger.warn("handleMissingServletRequestParameter handled: {}", ex.getMessage());

        Map<String, String> errors = new HashMap<>();
        errors.put(ex.getParameterName(), "Tham số này là bắt buộc.");

        ErrorResponse body = baseError(request, "Thiếu tham số bắt buộc.");
        body.setValidationErrors(errors);

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Xảy ra: Tham số URL/Query không đúng kiểu dữ liệu.
     * Các trường hợp:
     * - @PathVariable
     * - @RequestParam
     * - @RequestHeader
     * VD: getUser(@PathVariable Long id) với id = "abc" → gây TypeMismatchException.
     */
    public ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, WebRequest request) {
        logger.warn("handleTypeMismatch handled: {}", ex.getMessage());

        String paramName = ex.getPropertyName();
        String requiredType = (ex.getRequiredType() != null) ? ex.getRequiredType().getSimpleName() : "không xác định";

        Map<String, String> errors = new HashMap<>();
        errors.put(paramName, String.format("Không đúng định dạng. Yêu cầu kiểu '%s'.", requiredType));

        ErrorResponse body = baseError(request, "Tham số không đúng định dạng.");
        body.setValidationErrors(errors);

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Xảy ra: Client gửi JSON sai định dạng (HttpMessageNotReadableException).
     * Các trường hợp phổ biến:
     * 📌 Lỗi cú pháp JSON (thừa dấu phẩy, thiếu dấu đóng ngoặc)
     * 📌 Gửi sai kiểu dữ liệu (VD: yêu cầu số nhưng gửi chuỗi)
     * 📌 Gửi trường không tồn tại trong DTO (field không được nhận dạng)
     */
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, WebRequest request) {
        logger.warn("handleHttpMessageNotReadable handled: {}", ex.getMessage());

        String generalMessage = "Lỗi đọc dữ liệu JSON.";
        Map<String, String> errors = new HashMap<>();

        Throwable rootCause = ex.getMostSpecificCause();

        if (rootCause instanceof com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException) {
            String unknownField = ((com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException) rootCause).getPropertyName();
            errors.put(unknownField, "Trường không được nhận dạng.");
            generalMessage = "JSON chứa trường không hợp lệ.";
        } else if (rootCause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
            String fieldName = ((com.fasterxml.jackson.databind.exc.InvalidFormatException) rootCause).getPath().get(0).getFieldName();
            errors.put(fieldName, "Định dạng giá trị không hợp lệ.");
            generalMessage = "Kiểu dữ liệu JSON không hợp lệ.";
        } else if (rootCause instanceof com.fasterxml.jackson.core.JsonParseException) {
            errors.put("_json", "Lỗi cú pháp JSON. Vui lòng kiểm tra dấu ngoặc hoặc dấu phẩy.");
            generalMessage = "Lỗi cú pháp JSON.";
        } else {
            errors.put("_json", "Không thể đọc định dạng JSON.");
        }

        ErrorResponse body = baseError(request, generalMessage);
        body.setValidationErrors(errors);

        return ResponseEntity.badRequest().body(body);
    }


    // ==================================================================================================
    // 4. HANDLER DỰ PHÒNG (Fallback Handler)
    // ==================================================================================================

    /**
     * Bắt TẤT CẢ các ngoại lệ khác không được xử lý cụ thể.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllOtherExceptions(Exception ex, WebRequest request) {
        logger.error("Unhandled exception occurred: ", ex);

        ErrorResponse body = baseError(request,
                "Đã xảy ra lỗi không mong muốn. Vui lòng liên hệ hỗ trợ.");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    private ErrorResponse baseError(WebRequest request, String message) {
        return ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .path(getRequestPath(request))
                .message(message) // <-- Message phân loại lỗi
                .build();
    }

    private String getRequestPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}