package com.example.instagram.controller;

import com.example.instagram.dto.SignupRequestDto;
import com.example.instagram.dto.SignupResponseDto;
import com.example.instagram.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 회원 관련 REST API 컨트롤러
 */
@RestController  // RESTful API 컨트롤러
@RequestMapping("/api/user")  // 기본 URL 설정
@RequiredArgsConstructor  // UserService 주입을 위한 생성자 자동 생성 (Lombok)
public class UserController {

    private final UserService userService;  // 회원 서비스

    /**
     * 회원가입 API
     * - 클라이언트에서 회원 정보를 JSON으로 전송
     * - UserService를 호출하여 회원가입 처리
     * - 성공 시 HTTP 201 Created 응답
     */
    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> registerUser(
            @Valid @RequestBody SignupRequestDto signupRequestDto) {

        SignupResponseDto response = userService.signup(signupRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
