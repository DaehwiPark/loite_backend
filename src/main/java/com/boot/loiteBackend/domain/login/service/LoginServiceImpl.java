package com.boot.loiteBackend.domain.login.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.global.security.jwt.JwtCookieUtil;
import com.boot.loiteBackend.domain.login.dto.LoginRequestDto;
import com.boot.loiteBackend.domain.login.dto.LoginResponseDto;
import com.boot.loiteBackend.domain.login.error.LoginErrorCode;
import com.boot.loiteBackend.domain.token.service.TokenService;
import com.boot.loiteBackend.global.security.jwt.JwtTokenProvider;
import com.boot.loiteBackend.web.user.dto.UserSummaryDto;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import com.boot.loiteBackend.web.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PrivateKey;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final JwtCookieUtil jwtCookieUtil;
    private final JwtTokenProvider jwtTokenProvider;


    // 일반 로그인 처리
    @Override
    public LoginResponseDto login(LoginRequestDto dto, HttpServletResponse response, String userLoginType) {
        UserEntity user = userRepository.findByUserEmail(dto.getEmail())
                .orElseThrow(() -> new CustomException(LoginErrorCode.NOT_FOUND));

        if (!passwordEncoder.matches(dto.getPassword(), user.getUserPassword())) {
            throw new CustomException(LoginErrorCode.INVALID_PASSWORD);
        }

        // 로그인 방식 정보를 포함하여 토큰 생성 및 응답 처리
        return tokenService.getLoginToken(user, response, userLoginType);
    }


    // 로그아웃 처리 - RefreshToken 제거 + 쿠키 삭제
    @Override
    public void logout(CustomUserDetails userDetails, HttpServletResponse response) {
        String userId = String.valueOf(userDetails.getUserId());
        tokenService.deleteRefreshToken(userId);
        jwtCookieUtil.deleteAccessTokenCookie(response);
    }


    // 로그인한 사용자 정보 조회
    @Override
    @Transactional(readOnly = true)
    public UserSummaryDto myInfo(CustomUserDetails user, String token) {
        if (user == null) {
            throw new CustomException(LoginErrorCode.UNAUTHORIZED);
        }

        UserEntity userEntity = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new CustomException(LoginErrorCode.NOT_FOUND));

        String userLoginType = jwtTokenProvider.getUserLoginType(token);

        return UserSummaryDto.builder()
                .userId(userEntity.getUserId())
                .userEmail(userEntity.getUserEmail())
                .userName(userEntity.getUserName())
                .userRole(userEntity.getUserRole())
                .userRegisterType(userEntity.getUserRegisterType())
                .userLoginType(userLoginType)
                .build();
    }

    @Override
    public boolean check(CustomUserDetails user, String password) {
        if (user == null) {
            throw new CustomException(LoginErrorCode.UNAUTHORIZED);
        }

        UserEntity userEntity = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new CustomException(LoginErrorCode.NOT_FOUND));

        if (userEntity.getUserPassword() == null) {
            throw new CustomException(LoginErrorCode.SOCIAL_USER_CANNOT_VERIFY_PASSWORD);
        }

        if (!passwordEncoder.matches(password, userEntity.getUserPassword())) {
            throw new CustomException(LoginErrorCode.INVALID_PASSWORD);
        }

        return true;
    }
}
