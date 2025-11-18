1. Lỗi Nghiệp vụ (**BusinessException** - 404 Not Found)
   - **Trigger**: throw new BusinessException(ExceptionType.RESOURCE_NOT_FOUND, "Không tìm thấy Thương hiệu với ID: 999");
   - **Lưu ý**: Trường **`message`** là thông điệp chung **"Lỗi logic nghiệp vụ."**. Chi tiết lỗi nằm trong **`exType`**.
   - **Output**:
    ```json
       {
            "timestamp": "YYYY-MM-DD hh:mm:ss",
            "path": "/api/admin/brands/999",
            "message": "Lỗi logic nghiệp vụ.",
            "exType": {
                "code": "RESOURCE_NOT_FOUND",
                "httpStatus": "NOT_FOUND",
                "defaultMessage": "Tài nguyên không tìm thấy."
            }
       }
    ```

2. Lỗi **MethodArgumentNotValidException** (Validation DTO - @RequestBody)
   - **Trigger**: Gửi JSON: { "code": "", "name": "A" }
   - **Lưu ý**: Trường **`message`** là thông điệp phân loại **"Dữ liệu đầu vào không hợp lệ."**. Chi tiết lỗi nằm trong **`validationErrors`**.
   - **Output**:
    ```json
       {
            "timestamp": "YYYY-MM-DD hh:mm:ss",
            "path": "/api/admin/brands",
            "message": "Dữ liệu đầu vào không hợp lệ.",
            "validationErrors": {
                "code": "Mã thuộc tính không được để trống",
                "name": "Tên thuộc tính phải có độ dài từ 3 đến 50 ký tự." 
            }
       }
    ```

3. Lỗi **ConstraintViolationException** (Validation Tham số - @RequestParam, @PathVariable)
   - **Trigger**: Gọi API với tham số bị ràng buộc (ví dụ: `code` quá ngắn, `page` < 1).
   - **Lưu ý**: Trường **`message`** là thông điệp phân loại **"Lỗi ràng buộc tham số đầu vào."**. Chi tiết lỗi nằm trong **`validationErrors`**.
   - **Output**:
    ```json
       {
            "timestamp": "YYYY-MM-DD hh:mm:ss", 
            "path": "/api/admin/search-product", 
            "message": "Lỗi ràng buộc tham số đầu vào.",
            "validationErrors": {
                "searchRequest.code": "Mã phải có ít nhất 3 ký tự",
                "page": "Số trang phải lớn hơn 0"
            }
       }
    ```
   *Ghi chú: Tên trường trong `validationErrors` có thể bao gồm tên đối tượng cha (ví dụ: `searchRequest.code`) nếu validation áp dụng cho một đối tượng tham số trong phương thức.*

4. Lỗi **MissingServletRequestParameterException** (Thiếu @RequestParam)
   - **Trigger**: Thiếu @RequestParam (required=true) trong request.
   - **Lưu ý**: Trường **`message`** là thông điệp phân loại **"Thiếu tham số bắt buộc."**. Chi tiết lỗi nằm trong **`validationErrors`**.
   - **Output**:
    ```json
       {
            "timestamp": "YYYY-MM-DD hh:mm:ss", 
            "path": "/api/products", 
            "message": "Thiếu tham số bắt buộc.",
            "validationErrors": {
                "categoryId": "Tham số này là bắt buộc."
            }
       }
    ```

5. Lỗi **TypeMismatchException** (Sai kiểu dữ liệu tham số)
   - **Trigger**: Gửi "abc" cho tham số yêu cầu kiểu số (VD: `/api/products/abc`).
   - **Lưu ý**: Trường **`message`** là thông điệp phân loại **"Tham số không đúng định dạng."**. Chi tiết lỗi nằm trong **`validationErrors`**.
   - **Output**:
    ```json
       {
            "timestamp": "YYYY-MM-DD hh:mm:ss", 
            "path": "/api/products/abc", 
            "message": "Tham số không đúng định dạng.",
            "validationErrors": {
                "id": "Không đúng định dạng. Yêu cầu kiểu 'Long'."
            }
       }
    ```

6. Lỗi **HttpMessageNotReadableException** (Lỗi cú pháp JSON)
   - **Trigger**: Gửi JSON sai cú pháp hoặc sai kiểu dữ liệu (VD: `{"name": 123}` khi `name` là `String`).
   - **Lưu ý**: Trường **`message`** là thông điệp phân loại (ví dụ: "Lỗi cú pháp JSON." hoặc "JSON chứa trường không hợp lệ."). Chi tiết lỗi nằm trong **`validationErrors`** (sử dụng key như `_json` hoặc tên trường sai).
   - **Output (Ví dụ lỗi cú pháp):**
    ```json
       {
            "timestamp": "YYYY-MM-DD hh:mm:ss", 
            "path": "/api/brands", 
            "message": "Lỗi cú pháp JSON.",
            "validationErrors": {
                "_json": "Lỗi cú pháp JSON. Vui lòng kiểm tra dấu ngoặc hoặc dấu phẩy."
            }
       }
    ```

7. Lỗi **Exception** (Lỗi hệ thống - Fallback 500)
   - **Trigger**: Lỗi không mong muốn trong quá trình xử lý (NullPointerException, Database error, etc.).
   - **Lưu ý**: Trường **`message`** là thông điệp chung **"Đã xảy ra lỗi hệ thống không mong muốn. Vui lòng liên hệ hỗ trợ."**. Các trường `exType` và `validationErrors` sẽ là **null** (do `@JsonInclude(JsonInclude.Include.NON_NULL)`).
   - **Output**:
    ```json
       {
            "timestamp": "YYYY-MM-DD hh:mm:ss", 
            "path": "/api/some-endpoint", 
            "message": "Đã xảy ra lỗi hệ thống không mong muốn. Vui lòng liên hệ hỗ trợ."
       }
    ```