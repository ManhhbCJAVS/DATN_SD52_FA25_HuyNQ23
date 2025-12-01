package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@Getter
public class SignInRequest {
    @NotBlank(message = "Email is required")
    String email;
    @NotEmpty(message = "Password is required")
    String password;
}
