package com.example.instagram.service;

import com.example.instagram.domain.User;
import com.example.instagram.domain.enums.UserRole;
import com.example.instagram.dto.SignupRequestDto;
import com.example.instagram.dto.SignupResponseDto;
import com.example.instagram.exception.DuplicateEmailException;
import com.example.instagram.exception.DuplicateUsernameException;
import com.example.instagram.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * UserServiceImpl 테스트 클래스
 */
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // =========================
    // 회원가입 성공 테스트
    // =========================
    @Test
    @DisplayName("회원가입 성공")
    void signup_success() {
        // Given
        SignupRequestDto dto = SignupRequestDto.builder()
                .username("testuser")
                .password("password123")
                .email("test@example.com")
                .name("테스트유저")
                .phone("01012345678")
                .build();

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(dto.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(dto.getPassword())).thenReturn("encodedPassword");

        User savedUser = User.builder()
                .id(1L)
                .username(dto.getUsername())
                .password("encodedPassword")
                .email(dto.getEmail())
                .name(dto.getName())
                .phone(dto.getPhone())
                .role(UserRole.USER)
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        SignupResponseDto result = userService.signup(dto);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("테스트유저", result.getName());

        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
        verify(userRepository, times(1)).existsByUsername(dto.getUsername());
        verify(passwordEncoder, times(1)).encode(dto.getPassword());
        verify(userRepository, times(1)).save(any(User.class));
    }

    // =========================
    // 이메일 중복 실패 테스트
    // =========================
    @Test
    @DisplayName("회원가입 실패 - 이메일 중복")
    void signup_fail_email_exists() {
        // Given
        SignupRequestDto dto = SignupRequestDto.builder()
                .username("testuser")
                .password("password123")
                .email("test@example.com")
                .name("테스트유저")
                .build();

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(true);

        // When & Then
        DuplicateEmailException exception = assertThrows(
                DuplicateEmailException.class,
                () -> userService.signup(dto)
        );

        assertEquals("이미 사용 중인 이메일입니다.", exception.getMessage());

        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
        verify(userRepository, never()).existsByUsername(anyString());
        verify(userRepository, never()).save(any());
    }

    // =========================
    // 아이디(username) 중복 실패 테스트
    // =========================
    @Test
    @DisplayName("회원가입 실패 - 아이디 중복")
    void signup_fail_username_exists() {
        // Given
        SignupRequestDto dto = SignupRequestDto.builder()
                .username("testuser")
                .password("password123")
                .email("test@example.com")
                .name("테스트유저")
                .build();

        when(userRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(dto.getUsername())).thenReturn(true);

        // When & Then
        DuplicateUsernameException exception = assertThrows(
                DuplicateUsernameException.class,
                () -> userService.signup(dto)
        );

        assertEquals("이미 사용 중인 아이디입니다.", exception.getMessage());

        verify(userRepository, times(1)).existsByEmail(dto.getEmail());
        verify(userRepository, times(1)).existsByUsername(dto.getUsername());
        verify(userRepository, never()).save(any());
    }
}
