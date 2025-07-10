package com.boot.loiteBackend.domain.login.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.global.security.jwt.JwtCookieUtil;
import com.boot.loiteBackend.global.security.jwt.JwtTokenProvider;
import com.boot.loiteBackend.domain.login.dto.LoginRequestDto;
import com.boot.loiteBackend.domain.login.dto.LoginResponseDto;
import com.boot.loiteBackend.domain.login.error.LoginErrorCode;
import com.boot.loiteBackend.domain.token.service.TokenService;
import com.boot.loiteBackend.web.user.dto.UserSummaryDto;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import com.boot.loiteBackend.web.user.repository.UserRepository;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;
    private final JwtCookieUtil jwtCookieUtil;

    @Override
    public LoginResponseDto login(LoginRequestDto dto, HttpServletResponse response) {
        UserEntity user = userRepository.findByUserEmail(dto.getEmail())
                .orElseThrow(() -> new CustomException(LoginErrorCode.NOT_FOUND));

        if (!passwordEncoder.matches(dto.getPassword(), user.getUserPassword())) {
            throw new CustomException(LoginErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtTokenProvider.createToken(
                user.getUserId(),
                user.getUserEmail(),
                user.getUserRole(),
                user.getUserRegisterType()
        );
        return tokenService.getLoginToken(user, response);
    }

    @Override
    public void logout(CustomUserDetails userDetails, HttpServletResponse response) {
        String userId = String.valueOf(userDetails.getUserId());
        tokenService.deleteRefreshToken(userId);
        jwtCookieUtil.deleteAccessTokenCookie(response);
    }

    @Override
    @Transactional(readOnly = true)
    public UserSummaryDto myInfo(CustomUserDetails user) {
        if (user == null) {
            throw new CustomException(LoginErrorCode.UNAUTHORIZED);
        }

        UserEntity userEntity = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new CustomException(LoginErrorCode.NOT_FOUND));

        // 소셜 타입 결정 (소셜 회원인 경우만)
        String socialType = null;
        if (userEntity.getUserPassword() == null && !userEntity.getUserSocials().isEmpty()) {
            socialType = userEntity.getUserSocials().get(0).getSocialType(); // 첫 번째 소셜 타입 사용
        }

        return UserSummaryDto.builder()
                .userId(userEntity.getUserId())
                .userEmail(userEntity.getUserEmail())
                .userName(userEntity.getUserName())
                .userRole(userEntity.getUserRole())
                .userRegisterType(userEntity.getUserRegisterType())
                .build();
    }

}
