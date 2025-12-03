package edu.poly.datn_sd52_fa25_huynq203.admin;


import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.auth.SignInRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.auth.SignInResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.service.jwt.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("${backoffice.endpoint}")

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Validated
@Slf4j
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody SignInRequest signInRequest
    ) {
        log.info("Login request: {}", signInRequest);
        SignInResponse signInResponse = authenticationService.signIn(signInRequest);
        var accessTokenCookie = authenticationService.createTokenCookie(
                "staff_accessToken",
                signInResponse.getAccessToken(),
                15 * 60); // 15Minute
        var refreshTokenCookie = authenticationService.createTokenCookie(
                "staff_refreshToken",
                signInResponse.getRefreshToken(),
                60 * 60 * 24); // 7 days

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(signInResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        // 1. Tạo Cookie "xoá" cho Access Token
        var cleanAccess = authenticationService.cleanCookie("staff_accessToken");

        // 2. Tạo Cookie "xoá" cho Refresh Token
        var cleanRefresh = authenticationService.cleanCookie("refreshToken");

        // 3. Trả về Response kèm Header Set-Cookie để trình duyệt thực thi
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cleanAccess.toString())
                .header(HttpHeaders.SET_COOKIE, cleanRefresh.toString())
                .body("Logged out successfully!");
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword() {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword() {
        return ResponseEntity.ok().build();
    }
}
