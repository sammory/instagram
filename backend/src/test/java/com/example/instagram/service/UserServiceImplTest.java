package com.example.instagram.service;

import com.example.instagram.domain.user.User;
import com.example.instagram.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository; // 실제 DB를 사용하지 않고, 가짜(Mock) 객체를 사용하여 테스트 진행

    @Mock
    private BCryptPasswordEncoder passwordEncoder; // 실제 암호화 로직을 사용하지 않고, 가짜(Mock) 객체로 대체

    @InjectMocks
    private UserServiceImpl userService; // 실제 테스트할 `UserServiceImpl` 클래스 (Mock 객체들이 자동 주입됨)

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // 테스트 실행 전에 Mock 객체들을 초기화 (null 방지)
    }

    @Test
    @DisplayName("회원가입 성공 테스트") // 테스트의 목적을 설명
    void registerUser_success() {
        // Given (준비): 테스트에 사용할 가짜 User 객체 생성
        User user = User.builder()
                .username("testuser")
                .password("password123") // 원래 비밀번호 (아직 암호화되지 않음)
                .email("test@example.com")
                .name("테스트 유저")
                .gender((byte) 1)
                .build();

        // Mock 설정 (테스트 환경을 위한 가짜 동작 정의)
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty()); // 중복된 username 없음
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty()); // 중복된 email 없음
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword"); // 실제 암호화 대신, "encodedPassword" 반환
        when(userRepository.save(any(User.class))).thenReturn(user); // DB 저장 없이, 저장된 것처럼 user 반환

        // When (실행): 회원가입 진행
        User savedUser = userService.registerUser(user);

        // Then (검증): 회원가입이 정상적으로 완료되었는지 확인
        assertNotNull(savedUser); // 회원가입이 실패하면 null이 반환될 수 있음 → null이 아니어야 성공
        assertEquals("testuser", savedUser.getUsername()); // username이 올바르게 저장되었는지 검증
        assertEquals("test@example.com", savedUser.getEmail()); // email이 올바르게 저장되었는지 검증

        // Verify (Mock 객체가 실제로 예상한 만큼 호출되었는지 검증)
        verify(userRepository, times(1)).findByUsername(user.getUsername()); // username 중복 체크가 1번 실행되었는지 확인
        verify(userRepository, times(1)).findByEmail(user.getEmail()); // email 중복 체크가 1번 실행되었는지 확인
        verify(userRepository, times(1)).save(any(User.class)); // save() 메서드가 1번 실행되었는지 확인 (실제 DB 저장 대신 Mock 동작 검증)
    }

    @Test
    @DisplayName("회원가입 실패 테스트 - username 중복 예외 발생")
    void registerUser_fail_usernameExists_exception() {
        // Given (준비): 이미 존재하는 유저 설정
        User existingUser = User.builder()
                .username("testuser") // 이미 존재하는 username
                .password("password123")
                .email("test@example.com")
                .name("테스트 유저")
                .gender((byte) 1)
                .build();

        User newUser = User.builder()
                .username("testuser") // 같은 username을 가진 새 유저
                .password("newpassword")
                .email("newuser@example.com") // 다른 email
                .name("새로운 유저")
                .gender((byte) 1)
                .build();

        // Mock 설정: 기존 유저가 DB에 존재하는 경우
        when(userRepository.findByUsername(existingUser.getUsername()))
                .thenReturn(Optional.of(existingUser)); // 중복된 username 존재

        // When & Then (예외 검증)
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class, // 예외 발생해야 함
                () -> userService.registerUser(newUser) // 회원가입 시도
        );

        // 예외 메시지가 정확한지 검증
        assertEquals("이미 존재하는 username입니다.", exception.getMessage());

        // verify 검증: save()가 실행되지 않아야 함
        verify(userRepository, times(1)).findByUsername(newUser.getUsername()); // username 중복 체크 실행됨
        verify(userRepository, never()).findByEmail(anyString()); // email 체크는 실행되지 않아야 함
        verify(userRepository, never()).save(any(User.class)); // save()가 실행되지 않아야 함
    }

}
