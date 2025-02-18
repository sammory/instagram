package com.example.instagram.service;

import com.example.instagram.domain.user.User;
import com.example.instagram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder; // Spring Security 암호화 사용

    @Override
    @Transactional
    public User registerUser(User user) {
        // 사용자명 중복 체크
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 username입니다.");
        }
        // 이메일 중복 체크
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 email입니다.");
        }

        // 비밀번호 암호화
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // DB 저장
        return userRepository.save(user);
    }
}
