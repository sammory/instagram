package com.example.instagram.service;

import com.example.instagram.domain.user.User;

// 회원 관련 서비스 인터페이스
public interface UserService {

    // 회원가입 메서드
    User registerUser(User user);
}
