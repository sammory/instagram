package com.example.instagram.service;

import com.example.instagram.domain.user.User;
import com.example.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder; // Spring Security 암호화 사용

    @Override
    @Transactional
    public User registerUser(User user) {
        // 사용자명 중복 체크
        Optional<User> existingUserByUsername = userRepository.findByUsername(user.getUsername());
        if (existingUserByUsername.isPresent()) {
            return null; // 예외 처리는 나중에 적용 예정
        }

        // 이메일 중복 체크
        Optional<User> existingUserByEmail = userRepository.findByEmail(user.getEmail());
        if (existingUserByEmail.isPresent()) {
            return null; // 예외 처리는 나중에 적용 예정
        }

        // 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // DB 저장
        return userRepository.save(user);
    }
}
