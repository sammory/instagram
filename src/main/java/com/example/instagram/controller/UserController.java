package com.example.instagram.controller;

import com.example.instagram.domain.user.User;
import com.example.instagram.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // RESTful API 컨트롤러
@RequestMapping("/api/user") // 기본 URL 설정
@RequiredArgsConstructor // UserService 주입을 위한 생성자 자동 생성 (Lombok)
public class UserController {

    private final UserService userService; // 회원 서비스

    /**
     * 회원가입 API
     * - 클라이언트에서 회원 정보를 JSON으로 전송
     * - UserService를 호출하여 회원가입 처리
     * - 성공 시 HTTP 200 OK 응답
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User newUser = userService.registerUser(user);
        return ResponseEntity.ok(newUser);
    }
}
