package com.example.instagram.domain.user;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // PK (자동 증가)

    @Column(unique = true)
    private String username;  // 사용자 아이디

    private String password;  // 비밀번호 (암호화 필요)

    private String email;  // 이메일

    private String name;  // 이름

    private String website;  // 웹사이트 URL

    private String bio;  // 자기소개

    private String phone;  // 전화번호

    private Byte gender;  // 1: 남성, 2: 여성, 3: 기타 (TINYINT 사용)

    private String profileImage;  // 프로필 이미지 URL

    private Byte role;  // 사용자 역할 (1=USER, 2=ADMIN)

    private String provider;  // OAuth 제공자 (Google, Facebook 등)

    private String providerId;  // OAuth 제공자의 ID

    @CreationTimestamp
    private Timestamp createDate;  // 가입일
}
