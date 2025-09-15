package com.boot.loiteBackend.admin.login.service;

import com.boot.loiteBackend.admin.login.dto.AdminLoginRequestDto;
import com.boot.loiteBackend.admin.login.error.AdminLoginErrorCode;
import com.boot.loiteBackend.admin.user.dto.AdminUserSummaryDto;
import com.boot.loiteBackend.config.security.CustomUserDetails;
import com.boot.loiteBackend.config.security.jwt.JwtCookieUtil;
import com.boot.loiteBackend.config.security.jwt.JwtTokenProvider;
import com.boot.loiteBackend.domain.token.service.TokenService;
import com.boot.loiteBackend.domain.user.general.entity.UserEntity;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.login.error.LoginErrorCode;
import com.boot.loiteBackend.web.user.general.dto.UserSummaryDto;
import jakarta.servlet.http.Cookie;
// ↓ 실제 사용 중인 경로에 맞춰 하나만 사용하세요.
import com.boot.loiteBackend.web.user.general.repository.UserRepository;
// import com.boot.loiteBackend.domain.user.general.repository.UserRepository;

import com.boot.loiteBackend.web.login.dto.LoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminLoginServiceImpl implements AdminLoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtCookieUtil jwtCookieUtil;

    // 프로젝트 실제 코드값에 맞춰 수정하세요.
    private static final String STATUS_ACTIVE = "ACTIVE";
    private static final String ROLE_ADMIN   = "ADMIN";
    private static final String LOGIN_TYPE_EMAIL = "EMAIL"; // TokenService에 넘기는 userLoginType

    @Override
    @Transactional
    public LoginResponseDto login(AdminLoginRequestDto req, HttpServletResponse response) {
        UserEntity user = userRepository.findByUserEmailAndWithdrawnAtIsNull(req.getEmail())
                .orElseThrow(() -> new CustomException(AdminLoginErrorCode.USER_NOT_FOUND));

        // 상태 체크
        String statusCode = user.getUserStatus() != null ? user.getUserStatus().getStatusCode() : null;
        if (statusCode == null || !STATUS_ACTIVE.equalsIgnoreCase(statusCode)) {
            throw new CustomException(AdminLoginErrorCode.INACTIVE_STATUS);
        }

        // 관리자 권한 체크
        String roleCode = user.getUserRole() != null ? user.getUserRole().getRoleCode() : null;
        if (roleCode == null || !ROLE_ADMIN.equalsIgnoreCase(roleCode)) {
            throw new CustomException(AdminLoginErrorCode.FORBIDDEN_ROLE);
        }

        // 소셜 전용 계정 방지
        if (user.getUserPassword() == null || user.getUserPassword().isBlank()) {
            throw new CustomException(AdminLoginErrorCode.SOCIAL_ACCOUNT);
        }

        // 비밀번호 검증
        if (!passwordEncoder.matches(req.getPassword(), user.getUserPassword())) {
            throw new CustomException(AdminLoginErrorCode.INVALID_PASSWORD);
        }

        // 토큰 발급 (AccessToken은 Cookie로, RefreshToken은 응답 본문에)
        return tokenService.getLoginToken(user, response, LOGIN_TYPE_EMAIL);
    }

    @Override
    public void logout(CustomUserDetails userDetails, HttpServletResponse response) {
        // 1) Redis에서 refresh 토큰 제거
        if (userDetails != null) {
            String userId = String.valueOf(userDetails.getUserId());
            tokenService.deleteRefreshToken(userId);
        }

        // 2) 브라우저 쿠키 제거 (발급 시와 동일한 속성으로!)
        jwtCookieUtil.deleteAccessTokenCookie(response);
        jwtCookieUtil.deleteRefreshTokenCookie(response);

        // 3) (선택) 시큐리티 컨텍스트 정리
        SecurityContextHolder.clearContext();
    }

    private String extractAccessToken(HttpServletRequest request) {
        // 3-1) 쿠키에서 (JwtCookieUtil을 프로젝트에 맞게 활용)
        String cookieName = getAccessTokenCookieNameSafe();
        if (cookieName != null) {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie c : cookies) {
                    if (cookieName.equals(c.getName())) {
                        return c.getValue();
                    }
                }
            }
        }

        // 3-2) Authorization 헤더에서
        String auth = request.getHeader("Authorization");
        if (auth != null && auth.startsWith("Bearer ")) {
            return auth.substring(7);
        }
        return null;
    }

    /**
     * JwtCookieUtil에 쿠키 이름 getter가 있다면 사용하고,
     * 없으면 프로젝트에서 실제로 사용 중인 이름을 상수로 반환하세요.
     */
    private String getAccessTokenCookieNameSafe() {
        try {
            // 예: jwtCookieUtil.getAccessTokenCookieName() 같은 메서드가 있다면 사용
            return (String) JwtCookieUtil.class
                    .getMethod("getAccessTokenCookieName")
                    .invoke(jwtCookieUtil);
        } catch (Exception e) {
            // 실제 쿠키 이름을 아래에 맞춰주세요 (예: "ACCESS_TOKEN" 또는 "AccessToken")
            return "AccessToken";
        }
    }

    private void clearAccessTokenCookie(HttpServletResponse response) {
        // JwtCookieUtil에 clear 메서드가 있다면 그걸 사용하세요.
        try {
            JwtCookieUtil.class
                    .getMethod("clearAccessTokenCookie", HttpServletResponse.class)
                    .invoke(jwtCookieUtil, response);
            return;
        } catch (Exception ignore) { /* 메서드 없을 때 아래로 fallback */ }

        // fallback: 같은 이름/경로로 Max-Age=0 쿠키 셋
        Cookie cookie = new Cookie(getAccessTokenCookieNameSafe(), "");
        cookie.setPath("/");        // JwtCookieUtil에서 사용한 path와 동일해야 함
        cookie.setHttpOnly(true);
        cookie.setSecure(true);     // HTTPS만 사용하는 환경이면 유지
        cookie.setMaxAge(0);        // 즉시 만료
        // SameSite 설정이 필요하면 JwtCookieUtil과 동일하게 적용
        response.addCookie(cookie);
    }


    @Override
    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public AdminUserSummaryDto myInfo(CustomUserDetails user, String token) {
        if (user == null) {
            throw new CustomException(LoginErrorCode.UNAUTHORIZED);
        }

        UserEntity userEntity = userRepository.findById(user.getUserId())
                .orElseThrow(() -> new CustomException(LoginErrorCode.NOT_FOUND));

        String userLoginType = jwtTokenProvider.getUserLoginType(token);

        return AdminUserSummaryDto.builder()
                .userId(userEntity.getUserId())
                .userEmail(userEntity.getUserEmail())
                .userName(userEntity.getUserName())
                .userRole(userEntity.getUserRole() != null ? userEntity.getUserRole().getRoleCode() : null)
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
