package edu.poly.datn_sd52_fa25_huynq203.library.service.jwt;

import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.request.auth.SignInRequest;
import edu.poly.datn_sd52_fa25_huynq203.library.model.dto.response.auth.SignInResponse;
import edu.poly.datn_sd52_fa25_huynq203.library.model.entity.user.Employee;
import edu.poly.datn_sd52_fa25_huynq203.library.repository.user.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService {

    EmployeeRepository employeeRepository;
    AuthenticationProvider adminAuthenticationProvider;
    JwtService jwtService;

    public SignInResponse signIn(SignInRequest req) {
        AuthenticationManager authManager = new ProviderManager(adminAuthenticationProvider);
        // Exists & isActive => hợp lệ
        authManager.authenticate(new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        return buildUserLoginResponse(req.getEmail());
    }

    // build SignInResponse
    public SignInResponse buildUserLoginResponse(String email) {
        Employee employee = employeeRepository.findByEmail(email).get();

        String accessToken = jwtService.generateAccessToken(email);
        String refreshToken = jwtService.generateRefreshToken(email);

        return SignInResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(employee.getId())
                .build();
    }

    public ResponseCookie createTokenCookie(String cookieName, String value, long maxAgeSecs) {
        return ResponseCookie.from(cookieName, value)
                .httpOnly(true) // JS FE ko đọc đc cookie
                .path("/") //
                .maxAge(maxAgeSecs) // Nếu không set (hoặc set -1): Nó là Session Cookie. Tắt trình duyệt là mất.
                .sameSite("Lax") // GET thì gửi cookie. HttpMethod khác mà k phải miền website thì k gửi.
                .build();
    }

    // Tiện tay làm luôn hàm xóa Cookie cho chức năng Logout sau này
    public ResponseCookie cleanCookie(String name) {
        return ResponseCookie.from(name, "").path("/").maxAge(0).build();
    }

}
