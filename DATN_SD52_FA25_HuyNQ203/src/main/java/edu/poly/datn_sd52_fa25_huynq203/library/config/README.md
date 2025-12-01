Configuration includes:

1) CorsConfiguration 
    - Cross-Origin Resource Sharing: Chia sẻ tài nguyên giữa các nguồn (origin) khác nhau
    - “Origin” (nguồn): Gồm giao thức (protocol), tên miền (domain) và cổng (port)
    - Ví dụ: http://frontend:3000 và http://backend:8080 là hai origin khác nhau vì chúng có cổng khác nhau
    - Cơ chế của browser để bảo vệ người dùng khỏi các cuộc tấn công Cross-Site Request Forgery (CSRF) (Giả mạo yêu cầu từ một trang khác)
    - Mục đích: Cho phép hoặc chặn các yêu cầu từ các origin khác nhau
    - Ví dụ: Chặn yêu cầu từ https://evil.com đến https://mybank.com (khi mybank.com không cho phép)
    - Cấu hình: Chỉ định các origin được phép, các phương thức HTTP được phép (GET, POST, PUT, DELETE), và các header được phép
   
2) SwaggerConfig
   - Cấu hình Swagger để tạo tài liệu API tự động
   - Cung cấp giao diện người dùng để thử nghiệm các endpoint của API

3) SecurityConfig
    3.1) JwtConfig
    3.2) OAuth2Config
4) CloudinaryConfig
   - Hỗ trợ luu trữ và quản lý hình ảnh trên đám mây bằng dịch vụ Cloudinary
   - Cấu hình gồm: 
     + API Key
     + API Secret
     + Cloud Name 
   
5) MailConfig
    - Cấu hình dịch vụ gửi email
    - SMTP server settings
    - Port, username, password
    - From address
    - 
6) ThymeleafConfig
   - Cấu hình Thymeleaf làm engine để render view trong ứng dụng Spring Boot
   - Thiết lập template resolver, template engine, và view resolver

7) ModelMapperConfig
    - Cấu hình ModelMapper để ánh xạ giữa các đối tượng Java (DTOs và Entities)
    - Thiết lập các quy tắc ánh xạ tùy chỉnh nếu cần thiết
