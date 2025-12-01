package edu.poly.datn_sd52_fa25_huynq203.library.service.impl;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class JwtService {

    Key secretKeyAccessToken = Keys.hmacShaKeyFor(Base64.getDecoder().decode("123456789"));
    Key secretKeyRefreshToken = Keys.hmacShaKeyFor(Base64.getDecoder().decode("987654321"));

    public String generateAccessToken(String email) {
        return "Bearer:" + Jwts.builder()
                .setIssuer("http://localhost:8080/admin/login") // Issuer — ai là người phát hành token
                .setSubject(email) // sub — Token này đại diện cho user nào trong CSDL.
                .setIssuedAt(new Date())// iat — Thời gian token sinh ra
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))// exp — Thời gian token hết hạn: 15 phút
                .signWith(secretKeyAccessToken, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(String email) {
        return "Bearer:" + Jwts.builder()
                .setIssuer("http://localhost:8080/admin/login") // Issuer — ai là người phát hành token
                .setSubject(email) // sub — Token này đại diện cho user nào trong CSDL.
                .setIssuedAt(new Date())// iat — Thời gian token sinh ra
                .setExpiration(new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30))// exp — Thời gian token hết hạn: 30 ngày
                .signWith(secretKeyRefreshToken, SignatureAlgorithm.HS256)
                .compact();
    }
}
