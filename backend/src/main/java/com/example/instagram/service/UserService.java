package com.example.instagram.service;

import com.example.instagram.dto.LoginRequestDto;
import com.example.instagram.dto.LoginResponseDto;
import com.example.instagram.dto.SignupRequestDto;
import com.example.instagram.dto.SignupResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface UserService {

    SignupResponseDto signup(SignupRequestDto dto);

    LoginResponseDto login(LoginRequestDto dto, HttpServletResponse response);

    void logout(HttpServletRequest request, HttpServletResponse response);

    String refresh(HttpServletRequest request, HttpServletResponse response);
}
