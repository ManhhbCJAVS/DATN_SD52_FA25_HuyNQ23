package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Getter
@ToString
public class SignInRequest {
    @Schema(description = "Email đăng nhập", example = "admin@gmail.com")
    @NotBlank(message = "Email is required")
    String email;
    @Schema(description = "Mật khẩu", example = "123456")
    @NotEmpty(message = "Password is required")
    String password;
}
