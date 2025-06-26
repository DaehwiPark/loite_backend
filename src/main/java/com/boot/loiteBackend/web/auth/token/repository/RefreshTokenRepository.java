package com.boot.loiteBackend.web.auth.token.repository;

import com.boot.loiteBackend.web.auth.token.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByRefreshToken(String refreshToken);
    void deleteByUserId(Long userId);
}
