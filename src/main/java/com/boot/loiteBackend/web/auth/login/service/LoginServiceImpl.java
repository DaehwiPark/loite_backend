package com.boot.loiteBackend.web.auth.login.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.auth.login.dto.LoginRequestDto;
import com.boot.loiteBackend.web.auth.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.auth.login.error.LoginErrorCode;
import com.boot.loiteBackend.web.auth.token.service.RefreshTokenService;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import com.boot.loiteBackend.web.user.repository.UserRepository;
import com.boot.loiteBackend.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final RefreshTokenService refreshTokenService;

  @Override
  public LoginResponseDto login(LoginRequestDto dto) {

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

    // 4. Redis에 RefreshToken 저장
    String userId = String.valueOf(user.getUserId()); // 키: refreshToken:{email}
    long expirationSeconds = jwtTokenProvider.getRefreshTokenValidity() / 1000;

    refreshTokenService.saveRefreshToken(userId, refreshToken, expirationSeconds);

    // 5. 응답 반환
    return LoginResponseDto.builder()
      .accessToken(accessToken)
      .refreshToken(refreshToken)
      .tokenType("Bearer")
      .build();
  }
}