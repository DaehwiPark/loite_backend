package com.boot.loiteBackend.web.auth.token.redis;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("refreshToken")
public class RefreshToken {
    @Id
    private String username;

    private String token;

    @TimeToLive
    private Long expiration;
}
