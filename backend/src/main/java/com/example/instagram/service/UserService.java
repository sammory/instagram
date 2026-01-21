package com.example.instagram.service;

import com.example.instagram.dto.SignupRequestDto;
import com.example.instagram.dto.SignupResponseDto;

/**
 * 회원 관련 서비스 인터페이스
 */
public interface UserService {

    /**
     * 회원가입 메서드
     */
    SignupResponseDto signup(SignupRequestDto dto);
}
