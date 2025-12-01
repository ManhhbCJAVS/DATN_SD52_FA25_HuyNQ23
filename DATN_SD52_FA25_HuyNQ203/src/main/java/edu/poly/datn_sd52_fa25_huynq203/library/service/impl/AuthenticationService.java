package edu.poly.datn_sd52_fa25_huynq203.library.service.impl;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.auth.SignInRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.auth.SignInResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService {

    AuthenticationProvider adminAuthenticationProvider;
    JwtService jwtService;

    public SignInResponse signIn(SignInRequest req) {
        AuthenticationManager authManager = new ProviderManager(adminAuthenticationProvider);
        // Exists & Active => hợp lệ
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

        String accessToken = jwtService.generateAccessToken(req.getEmail());
        String refreshToken = jwtService.generateRefreshToken(req.getEmail());

        return new SignInResponse(accessToken, refreshToken, null);
    }
}
