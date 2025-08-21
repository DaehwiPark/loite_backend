package com.boot.loiteBackend.config.security.jwt;

import com.boot.loiteBackend.config.security.cookie.CookieProperties;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class JwtCookieUtil {

    private final CookieProperties props;

    public JwtCookieUtil(CookieProperties props) {
        this.props = props;
    }

    public void addAccessTokenCookie(HttpServletResponse response, String token, long maxAgeSeconds) {
        ResponseCookie cookie = ResponseCookie.from(props.getAccessName(), token)
                .httpOnly(true)
                .secure(props.isSecure())
                .path(props.getPath())
                .domain(nullIfBlank(props.getDomain()))
                .sameSite(props.getSameSite())
                .maxAge(Duration.ofSeconds(maxAgeSeconds))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void deleteAccessTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(props.getAccessName(), "")
                .httpOnly(true)
                .secure(props.isSecure())
                .path(props.getPath())
                .domain(nullIfBlank(props.getDomain()))
                .sameSite(props.getSameSite())
                .maxAge(Duration.ZERO) // 즉시 만료
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(props.getRefreshName(), "")
                .httpOnly(true)
                .secure(props.isSecure())
                .path(props.getPath())
                .domain(nullIfBlank(props.getDomain()))
                .sameSite(props.getSameSite())
                .maxAge(Duration.ZERO) // 즉시 만료
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private String nullIfBlank(String v) {
        return (v == null || v.isBlank()) ? null : v;
    }
}