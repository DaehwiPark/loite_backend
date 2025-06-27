package com.boot.loiteBackend.web.auth.login.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.auth.login.dto.LoginRequestDto;
import com.boot.loiteBackend.web.auth.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.auth.login.error.LoginErrorCode;
import com.boot.loiteBackend.web.auth.token.entity.RefreshTokenEntity;
import com.boot.loiteBackend.web.auth.token.repository.RefreshTokenRepository;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import com.boot.loiteBackend.web.user.repository.UserRepository;
import com.boot.loiteBackend.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {

        // 1. 사용자 조회
        UserEntity user = userRepository.findByUserEmail(dto.getEmail())
                .orElseThrow(() -> new CustomException(LoginErrorCode.NOT_FOUND));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(dto.getPassword(), user.getUserPassword())) {
            throw new CustomException(LoginErrorCode.INVALID_PASSWORD);
        }

        // 3. AccessToken + RefreshToken 생성 (이메일 추가)
        String accessToken = jwtTokenProvider.createToken(
                user.getUserId(),
                user.getUserEmail(),   // username 필드로 사용
                user.getRole()
        );

        String refreshToken = jwtTokenProvider.createRefreshToken();

        // 4. Refresh Token 만료 시각 계산
        LocalDateTime expiry = LocalDateTime.now()
                .plus(Duration.ofMillis(jwtTokenProvider.getRefreshTokenValidity()));

        // 5. Refresh Token 저장 (Upsert)
        RefreshTokenEntity tokenEntity = RefreshTokenEntity.builder()
                .userId(user.getUserId())
                .refreshToken(refreshToken)
                .expiryDate(expiry)
                .build();
        refreshTokenRepository.save(tokenEntity);

        // 6. 응답 반환
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }
}
