package com.boot.loiteBackend.web.login.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.config.security.CustomUserDetails;
import com.boot.loiteBackend.config.security.jwt.JwtCookieUtil;
import com.boot.loiteBackend.web.login.dto.LoginRequestDto;
import com.boot.loiteBackend.web.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.login.error.LoginErrorCode;
import com.boot.loiteBackend.domain.token.service.TokenService;
import com.boot.loiteBackend.config.security.jwt.JwtTokenProvider;
import com.boot.loiteBackend.web.user.general.dto.UserSummaryDto;
import com.boot.loiteBackend.domain.user.general.entity.UserEntity;
import com.boot.loiteBackend.web.user.general.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final JwtCookieUtil jwtCookieUtil;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public LoginResponseDto login(LoginRequestDto dto, HttpServletResponse response, String userLoginType) {
        UserEntity user = userRepository.findByUserEmail(dto.getEmail())
                .orElseThrow(() -> new CustomException(LoginErrorCode.NOT_FOUND));

        if (!passwordEncoder.matches(dto.getPassword(), user.getUserPassword())) {
            throw new CustomException(LoginErrorCode.INVALID_PASSWORD);
        }

        return tokenService.getLoginToken(user, response, userLoginType);
    }

    @Override
    public void logout(CustomUserDetails userDetails, HttpServletResponse response) {
        String userId = String.valueOf(userDetails.getUserId());
        // redis 에서 token 저장 정보 삭제
        tokenService.deleteRefreshToken(userId);
        // 브라우저에서 AccessToken 삭제
        jwtCookieUtil.deleteAccessTokenCookie(response);
        // 브라우저에서 RefreshToken 삭제
        jwtCookieUtil.deleteRefreshTokenCookie(response);

    }

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
                .userRole(userEntity.getUserRole() != null ? userEntity.getUserRole().getRoleName() : null)
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