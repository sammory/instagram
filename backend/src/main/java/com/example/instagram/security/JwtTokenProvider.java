package com.example.instagram.security;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;

    // JWT 토큰 생성
    public String generateToken(String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(username)              // 토큰에 username 저장
                .setIssuedAt(now)                 // 발급 시간
                .setExpiration(expiryDate)        // 만료 시간
                .signWith(SignatureAlgorithm.HS512, jwtSecret) // 서명
                .compact();
    }

    // 토큰에서 username 추출
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("JWT 만료됨: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("지원하지 않는 JWT: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("잘못된 형식의 JWT: " + e.getMessage());
        } catch (SignatureException e) {
            System.out.println("JWT 서명 오류: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims 비어있음: " + e.getMessage());
        }
        return false;
    }
}
