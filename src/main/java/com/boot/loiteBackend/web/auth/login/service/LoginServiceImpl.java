package com.boot.loiteBackend.web.auth.login.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.global.security.jwt.JwtCookieUtil;
import com.boot.loiteBackend.global.security.jwt.JwtTokenProvider;
import com.boot.loiteBackend.web.auth.login.dto.LoginRequestDto;
import com.boot.loiteBackend.web.auth.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.auth.login.error.LoginErrorCode;
import com.boot.loiteBackend.web.auth.token.service.TokenService;
import com.boot.loiteBackend.web.user.dto.UserSummaryDto;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import com.boot.loiteBackend.web.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;
    private final JwtCookieUtil jwtCookieUtil;

    @Override
    public LoginResponseDto login(LoginRequestDto dto, HttpServletResponse response) {
        // 1. 사용자 조회
        UserEntity user = userRepository.findByUserEmail(dto.getEmail())
                .orElseThrow(() -> new CustomException(LoginErrorCode.NOT_FOUND));

        // 2. 비밀번호 검증
        if (!passwordEncoder.matches(dto.getPassword(), user.getUserPassword())) {
            throw new CustomException(LoginErrorCode.INVALID_PASSWORD);
        }

        // 3. 토큰 생성
        String accessToken = jwtTokenProvider.createToken(
                user.getUserId(),
                user.getUserEmail(),
                user.getRole()
        );
        String refreshToken = jwtTokenProvider.createRefreshToken(user.getUserId());

        // 4. RefreshToken 저장 (Redis)
        String userId = String.valueOf(user.getUserId());
        long expirationSeconds = jwtTokenProvider.getRefreshTokenValidity() / 1000;
        tokenService.saveRefreshToken(userId, refreshToken, expirationSeconds);

        // 5. AccessToken 쿠키로 설정
        jwtCookieUtil.addAccessTokenCookie(response, accessToken, jwtTokenProvider.getAccessTokenValidity() / 1000);

        // 6. 응답 반환
        return LoginResponseDto.builder()
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public void logout(CustomUserDetails userDetails, HttpServletResponse response) {
        String userId = String.valueOf(userDetails.getUserId());
        tokenService.deleteRefreshToken(userId);
        jwtCookieUtil.deleteAccessTokenCookie(response);
    }

    @Override
    public UserSummaryDto myInfo(CustomUserDetails user) {
        if (user == null) {
            throw new CustomException(LoginErrorCode.UNAUTHORIZED);
        }

        UserEntity userEntity = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new CustomException(LoginErrorCode.NOT_FOUND));

        return UserSummaryDto.builder()
                .userId(user.getUserId())
                .userName(userEntity.getUserName())
                .userEmail(userEntity.getUserEmail())
                .role(user.getRole())
                .build();
    }
}
