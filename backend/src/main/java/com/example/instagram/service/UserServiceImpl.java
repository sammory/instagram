package com.example.instagram.service;

import com.example.instagram.domain.User;
import com.example.instagram.domain.enums.UserRole;
import com.example.instagram.dto.SignupRequestDto;
import com.example.instagram.dto.SignupResponseDto;
import com.example.instagram.exception.DuplicateEmailException;
import com.example.instagram.exception.DuplicateUsernameException;
import com.example.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 회원 관련 서비스 구현체
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  // 기본적으로 읽기 전용 트랜잭션
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;  // BCryptPasswordEncoder가 주입됨

    /**
     * 회원가입 메서드
     * - 이메일 및 아이디 중복 체크
     * - 비밀번호 암호화
     * - User 엔티티 생성 및 저장
     */
    @Override
    @Transactional  // 쓰기 작업이므로 트랜잭션 활성화
    public SignupResponseDto signup(SignupRequestDto dto) {
        // 중복 검증
        validateDuplicates(dto);

        // User 객체 생성
        User user = User.builder()
                .username(dto.getUsername())
                .password(passwordEncoder.encode(dto.getPassword()))  // 비밀번호 암호화
                .email(dto.getEmail())
                .name(dto.getName())
                .phone(dto.getPhone())
                .role(UserRole.USER)  // 일반 사용자로 설정
                .build();

        User savedUser = userRepository.save(user);

        return SignupResponseDto.from(savedUser);
    }

    /**
     * 이메일 및 아이디 중복 검증
     */
    private void validateDuplicates(SignupRequestDto dto) {
        // 이메일 중복 체크
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateEmailException("이미 사용 중인 이메일입니다.");
        }

        // 아이디(username) 중복 체크
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new DuplicateUsernameException("이미 사용 중인 아이디입니다.");
        }
    }
}
