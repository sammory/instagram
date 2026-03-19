package com.example.instagram.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "refreshToken", timeToLive = 604800) // 7일 (초 단위)
public class RefreshToken {

    @Id
    private String email; // Redis key

    private String token;
}
