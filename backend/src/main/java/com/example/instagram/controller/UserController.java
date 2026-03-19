package com.example.instagram.controller;

import com.example.instagram.dto.LoginRequestDto;
import com.example.instagram.dto.LoginResponseDto;
import com.example.instagram.dto.SignupRequestDto;
import com.example.instagram.dto.SignupResponseDto;
import com.example.instagram.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(
            @Valid @RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.signup(signupRequestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto loginRequestDto,
            HttpServletResponse response) {
        return ResponseEntity.ok(userService.login(loginRequestDto, response));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletRequest request,
            HttpServletResponse response) {
        userService.logout(request, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(
            HttpServletRequest request,
            HttpServletResponse response) {
        String newAccessToken = userService.refresh(request, response);
        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }
}
