package edu.poly.datn_sd52_fa25_huynq203.library.exception.handler;

import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.BusinessException;
import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.ErrorResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.ExceptionType;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
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

        ErrorResponse body = ErrorResponse.builder().timestamp(LocalDateTime.now()).path(getRequestPath(request)).status(ex.getExType().getStatus()).code(ex.getExType().getCode()).businessError(ex.getMessage() != null ? ex.getMessage() : ex.getExType().getDefaultMessage()).messageExceptionType("Lỗi logic nghiệp vụ.").build();

        return ResponseEntity.status(ex.getExType().getStatus()).body(body);
    }
    // =========================================================================
    // 5. HANDLER CHO SECURITY (CẦU NỐI)
    // =========================================================================

    /**
     * InternalAuthenticationServiceException: ném ra bởi DaoAuthenticationProvider :
     * khi loadUserByUsername gặp sự cố => Nó sẽ  bọc (wrap) exception trong loadUserByUsername TRỪ UsernameNotFoundException
     */
    @ExceptionHandler({InternalAuthenticationServiceException.class, BadCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleInternalAuthenticationServiceException(Exception ex, WebRequest request) {
        BusinessException targetException;
        // CASE 1: Exception bọc bởi InternalAuthenticationServiceException
        if (ex != null && ex.getCause() instanceof BusinessException) {
            targetException = (BusinessException) ex.getCause(); // để giữ nguyên message "Admin not found..."
        }
        // CASE 2: Nếu là lỗi sai mật khẩu (BadCredentials) hoặc lỗi khác
        else {
            targetException = new BusinessException(ExceptionType.INVALID_CREDENTIALS, "Email hoặc mật khẩu không chính xác");
        }
        return handleBusinessException(targetException, request);
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
        // Lấy thông tin lỗi từ BindingResult
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));

        ErrorResponse body = buildErrorResponse(request, "Dữ liệu đầu vào không hợp lệ.", HttpStatus.BAD_REQUEST.value(), "INVALID_REQUEST_BODY");
        body.setValidationErrors(errors);
        return ResponseEntity.badRequest().body(body);
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

        ErrorResponse body = buildErrorResponse(request, "Lỗi ràng buộc tham số đầu vào.", HttpStatus.BAD_REQUEST.value(), "INVALID_REQUEST_PARAMETER");
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

        ErrorResponse body = buildErrorResponse(request, "Thiếu tham số bắt buộc.", HttpStatus.BAD_REQUEST.value(), "MISSING_REQUEST_PARAMETER");
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
    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, WebRequest request) {
        logger.warn("handleTypeMismatch handled: {}", ex.getMessage());

        String paramName = ex.getPropertyName();
        String requiredType = (ex.getRequiredType() != null) ? ex.getRequiredType().getSimpleName() : "không xác định";

        Map<String, String> errors = new HashMap<>();
        errors.put(paramName, String.format("Không đúng định dạng. Yêu cầu kiểu '%s'.", requiredType));

        ErrorResponse body = buildErrorResponse(request, "Tham số không đúng định dạng.", HttpStatus.BAD_REQUEST.value(), "TYPE_MISMATCH");
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
    @ExceptionHandler(HttpMessageNotReadableException.class)
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

        ErrorResponse body = buildErrorResponse(request, generalMessage, HttpStatus.BAD_REQUEST.value(), "INVALID_JSON");
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

        ErrorResponse body = buildErrorResponse(request, "Đã xảy ra lỗi không mong muốn. Vui lòng liên hệ hỗ trợ.", HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_SERVER_ERROR");

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

    /**
     * @param messageExceptionType: Thông điệp chung cho loại Exception.
     */
    private ErrorResponse buildErrorResponse(WebRequest request, String messageExceptionType, int status, String code) {
        return ErrorResponse.builder().timestamp(LocalDateTime.now()).path(getRequestPath(request)).messageExceptionType(messageExceptionType).status(status).code(code).build();
    }

    private String getRequestPath(WebRequest request) {
        return request.getDescription(false).replace("uri=", "");
    }
}