package com.example.instagram.service;

import com.example.instagram.domain.RefreshToken;
import com.example.instagram.domain.User;
import com.example.instagram.domain.enums.UserRole;
import com.example.instagram.dto.LoginRequestDto;
import com.example.instagram.dto.LoginResponseDto;
import com.example.instagram.dto.SignupRequestDto;
import com.example.instagram.dto.SignupResponseDto;
import com.example.instagram.exception.DuplicateEmailException;
import com.example.instagram.exception.DuplicateUsernameException;
import com.example.instagram.exception.InvalidCredentialsException;
import com.example.instagram.repository.RefreshTokenRepository;
import com.example.instagram.repository.UserRepository;
import com.example.instagram.security.JwtTokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    @Transactional
    public SignupResponseDto signup(SignupRequestDto dto) {
        validateDuplicates(dto);

        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .name(dto.getName())
                .phone(dto.getPhone())
                .role(UserRole.USER)
                .build();

        return SignupResponseDto.from(userRepository.save(user));
    }

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto dto, HttpServletResponse response) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user.getEmail());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getEmail());

        // Refresh Token Redis 저장
        refreshTokenRepository.save(new RefreshToken(user.getEmail(), refreshToken));

        // Refresh Token httpOnly 쿠키 설정
        setRefreshTokenCookie(response, refreshToken);

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .email(user.getEmail())
                .username(user.getUsername())
                .name(user.getName())
                .build();
    }

    @Override
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookie(request);

        if (refreshToken != null && jwtTokenProvider.validateToken(refreshToken)) {
            String email = jwtTokenProvider.getEmailFromToken(refreshToken);
            refreshTokenRepository.deleteById(email); // Redis에서 삭제
        }

        clearRefreshTokenCookie(response);
    }

    @Override
    @Transactional
    public String refresh(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = extractRefreshTokenFromCookie(request);

        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            throw new InvalidCredentialsException("유효하지 않은 리프레시 토큰입니다.");
        }

        String email = jwtTokenProvider.getEmailFromToken(refreshToken);

        // Redis에 저장된 토큰과 일치 여부 확인
        RefreshToken storedToken = refreshTokenRepository.findById(email)
                .orElseThrow(() -> new InvalidCredentialsException("리프레시 토큰이 만료되었습니다."));

        if (!storedToken.getToken().equals(refreshToken)) {
            throw new InvalidCredentialsException("유효하지 않은 리프레시 토큰입니다.");
        }

        // Access Token 재발급 + Refresh Token Rotation
        String newAccessToken = jwtTokenProvider.generateAccessToken(email);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(email);

        refreshTokenRepository.save(new RefreshToken(email, newRefreshToken));
        setRefreshTokenCookie(response, newRefreshToken);

        return newAccessToken;
    }

    // ===== 헬퍼 메서드 =====

    private void validateDuplicates(SignupRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
        }
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateUsernameException("이미 사용 중인 아이디입니다.");
        }
    }

    private void setRefreshTokenCookie(HttpServletResponse response, String token) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", token)
                .httpOnly(true)
                .secure(false)       // 운영 환경에서는 true (HTTPS)
                .path("/api/user")
                .maxAge(Duration.ofDays(7))
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void clearRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(false)
                .path("/api/user")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private String extractRefreshTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;
        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
