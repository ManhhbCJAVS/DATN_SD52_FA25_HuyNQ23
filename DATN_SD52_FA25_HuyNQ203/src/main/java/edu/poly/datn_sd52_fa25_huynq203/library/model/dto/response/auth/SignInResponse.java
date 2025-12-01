package edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@AllArgsConstructor
public class SignInResponse {
    String accessToken;
    String refreshToken;
    UserLoginResponse userLoginResponse;
}
