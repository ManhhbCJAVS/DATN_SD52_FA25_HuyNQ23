package edu.poly.datn_sd52_fa25_huynq203.library.service.jwt;

import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.BusinessException;
import edu.poly.datn_sd52_fa25_huynq203.library.exception.base.ExceptionType;
import edu.poly.datn_sd52_fa25_huynq203.library.model.enums.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class JwtService {


    public String generateAccessToken(String email) {
        return Jwts.builder()
                .setIssuer("http://localhost:8080/admin/login") // Issuer — ai là người phát hành token
                .setSubject(email) // sub — Token này đại diện cho user nào trong CSDL.
                .setIssuedAt(new Date())// iat — Thời gian token sinh ra
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))// exp — Thời gian token hết hạn: 15 phút
                .signWith(getKey(TokenType.ACCESS_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email) {
        return Jwts.builder()
                .setIssuer("http://localhost:8080/admin/login") // Issuer — ai là người phát hành token
                .setSubject(email) // sub — Token này đại diện cho user nào trong CSDL.
                .setIssuedAt(new Date())// iat — Thời gian token sinh ra
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24))// exp — Thời gian token hết hạn: 1 ngày
                .signWith(getKey(TokenType.REFRESH_TOKEN), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Kiểm tra Token có hợp lệ với UserDetails ko
     */
    public boolean isTokenValid(String token, TokenType type, UserDetails userDetails) {
        final String email = extractEmail(token, type);
        // Token hợp lệ khi: Email khớp DB VÀ Token chưa hết hạn
        return (email.equals(userDetails.getUsername())) && !isTokenExpired(token, type);
    }

    // Token hết hạn?
    private boolean isTokenExpired(String token, TokenType type) {
        return extractClaim(token, type, Claims::getExpiration).before(new Date());
    }

    // Trích xuất Email (Subject) từ Token
    public String extractEmail(String token, TokenType tokenType) {
        return Optional.ofNullable(extractClaim(token, tokenType, Claims::getSubject))
                .orElseThrow(() -> new BusinessException(ExceptionType.INVALID_TOKEN, "Token ko hợp lệ: ko có Email"));
    }


    /**
     * Trích xuất một Claim bất kỳ
     */
    public <T> T extractClaim(String token, TokenType tokenType, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token, tokenType);
        return claimsResolver.apply(claims);
    }

    /**
     * Parse token và trả về Claims (Payload) nếu hợp lệ.
     * Hàm này sẽ tự động ném ra các Exception của thư viện JJWT nếu:
     * - ExpiredJwtException: Hết hạn
     * - MalformedJwtException: Sai cấu trúc
     * - SignatureException: Sai chữ ký
     */
    public Claims extractAllClaims(String token, TokenType tokenType) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey(tokenType))
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new BusinessException(ExceptionType.TOKEN_EXPIRED);
        } catch (MalformedJwtException | io.jsonwebtoken.security.SignatureException | IllegalArgumentException e) {
            throw new BusinessException(ExceptionType.INVALID_TOKEN);
        }
    }

    private Key getKey(TokenType type) {
        return switch (type) {
            case ACCESS_TOKEN -> Keys.hmacShaKeyFor("12345678901234567890123456789012".getBytes());
            case REFRESH_TOKEN -> Keys.hmacShaKeyFor("98765432101234567890123456789012".getBytes());
            default -> throw new BusinessException(ExceptionType.INVALID_TOKEN, "Loại Token không được hỗ trợ");
        };
    }


}
