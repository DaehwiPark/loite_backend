// 📁 com.boot.loiteBackend.web.auth.token.service.RefreshTokenService

package com.boot.loiteBackend.web.auth.token.service;

import com.boot.loiteBackend.web.auth.token.redis.RefreshToken;
import com.boot.loiteBackend.web.auth.token.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public void saveRefreshToken(String username, String token, long expirationSeconds) {
        RefreshToken refreshToken = new RefreshToken(username, token, expirationSeconds);
        refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> getRefreshToken(String username) {
        return refreshTokenRepository.findById(username);
    }

    public void deleteRefreshToken(String username) {
        refreshTokenRepository.deleteById(username);
    }
}
