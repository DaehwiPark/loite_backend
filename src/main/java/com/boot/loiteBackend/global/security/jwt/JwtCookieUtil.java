package com.boot.loiteBackend.global.security.jwt;

import com.boot.loiteBackend.global.config.AppEnvironmentConfig;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class JwtCookieUtil {

    private final AppEnvironmentConfig env;

    public JwtCookieUtil(AppEnvironmentConfig env) {
        this.env = env;
    }

    public void addAccessTokenCookie(HttpServletResponse response, String token, long maxAgeSeconds) {
        Cookie cookie = new Cookie("AccessToken", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge((int) maxAgeSeconds);
        cookie.setSecure(env.isSecureCookieEnabled());
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);
    }

    public void deleteAccessTokenCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("AccessToken", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0); // 즉시 만료
        cookie.setSecure(env.isSecureCookieEnabled());
        cookie.setAttribute("SameSite", "Strict");
        response.addCookie(cookie);
    }
}
