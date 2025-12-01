package edu.poly.datn_sd52_fa25_huynq203.admin;


import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.auth.SignInRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.auth.SignInResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.service.impl.AuthenticationService;
import edu.poly.datn_sd52_fa25_huynq203.library.service.impl.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/auth")

@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Validated
public class AuthenticationController {

    AuthenticationService authenticationService;
    JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody SignInRequest signInRequest
    ) {
        SignInResponse signInResponse = authenticationService.signIn(signInRequest);
        ResponseCookie accessTokenCookie = ResponseCookie.from("staff_accessToken", jwtService.generateAccessToken(signInRequest.getEmail()))
                .path("/")
                .httpOnly(true)
                .build();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", authenticationService.signIn(signInRequest).getRefreshToken())
                .path("/")
                .httpOnly(true)
                .build();

        return null;
    }

}
