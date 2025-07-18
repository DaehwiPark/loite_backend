package com.boot.loiteBackend.global.security.filter;

import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.global.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = null;

        // 1. AccessToken 쿠키에서 추출
        if (request.getCookies() != null) {
            token = Arrays.stream(request.getCookies())
                    .filter(cookie -> "AccessToken".equals(cookie.getName()))
                    .findFirst()
                    .map(Cookie::getValue)
                    .orElse(null);
        }

        // 2. 토큰 유효성 검사 및 사용자 정보 추출
        if (token != null && jwtTokenProvider.validateToken(token)) {
            try {
                String tokenType = jwtTokenProvider.getTokenType(token);

                // tokenType이 "access"인 경우만 인증 처리
                if (!"access".equals(tokenType)) {
                    logger.debug("JWT token is not access type. Skipping authentication.");
                    filterChain.doFilter(request, response);
                    return;
                }

                Long userId = jwtTokenProvider.getUserId(token);
                String role = jwtTokenProvider.getUserRole(token);
                String username = jwtTokenProvider.getUsername(token);
                String userRegisterType = jwtTokenProvider.getUserRegisterType(token); // ⭐️ 추가된 부분

                // 3. UserDetails 생성
                CustomUserDetails userDetails = new CustomUserDetails(
                        userId, role, username, userRegisterType
                );

                // 4. 인증 객체 생성 및 SecurityContext 설정
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (JwtException | IllegalArgumentException e) {
                logger.warn("Invalid JWT token", e);
            }
        }

        // 5. 다음 필터로 전달
        filterChain.doFilter(request, response);
    }
}
