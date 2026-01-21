package com.example.instagram.domain;

import com.example.instagram.domain.enums.Gender;
import com.example.instagram.domain.enums.UserRole;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // JPA를 위한 기본 생성자 (외부 생성 차단)
@AllArgsConstructor(access = AccessLevel.PRIVATE)  // Builder를 통해서만 생성 가능
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // PK (자동 증가)

    @Column(unique = true, nullable = false)
    private String username;  // 사용자 아이디

    @Column(nullable = false)
    private String password;  // 비밀번호 (암호화 필요)

    @Column(unique = true, nullable = false)
    private String email;  // 이메일
    private String name;  // 이름
    private String website;  // 웹사이트 URL
    private String bio;  // 자기소개
    private String phone;  // 전화번호

    @Enumerated(EnumType.STRING)
    private Gender gender;  // 성별 (MALE, FEMALE, OTHER)
    private String profileImage;  // 프로필 이미지 URL

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;  // 사용자 역할 (USER, ADMIN)
    private String provider;  // OAuth 제공자 (Google, Facebook 등)
    private String providerId;  // OAuth 제공자의 ID

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createDate;  // 가입일

    // === 비즈니스 메서드 ===

    /**
     * 프로필 정보 수정
     */
    public void updateProfile(String name, String bio, String website) {
        this.name = name;
        this.bio = bio;
        this.website = website;
    }

    /**
     * 프로필 이미지 수정
     */
    public void updateProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
