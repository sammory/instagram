package com.example.instagram.dto;

import com.example.instagram.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 회원가입 응답 DTO
 * - 비밀번호 등 민감한 정보는 제외하고 반환
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignupResponseDto {

    private Long id;  // 사용자 ID
    private String username;  // 사용자 아이디
    private String email;  // 이메일
    private String name;  // 이름
    private Timestamp createDate;  // 가입일

    /**
     * User 엔티티로부터 DTO 생성
     */
    public static SignupResponseDto from(User user) {
        return SignupResponseDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .createDate(user.getCreateDate())
                .build();
    }
}
