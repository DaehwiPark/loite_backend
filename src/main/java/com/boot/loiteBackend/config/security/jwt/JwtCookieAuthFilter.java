//package com.boot.loiteBackend.config.security.jwt;
//
//import com.boot.loiteBackend.config.security.CustomUserDetails; // ✅ 추가
//import com.boot.loiteBackend.config.security.cookie.CookieProperties;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class JwtCookieAuthFilter extends OncePerRequestFilter {
//
//    private static final Logger log = LoggerFactory.getLogger(JwtCookieAuthFilter.class);
//
//    private final JwtTokenProvider jwtTokenProvider;
//    private final CookieProperties cookieProps; // ★ 쿠키 이름/속성은 여기서 읽음
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) {
//        String uri = request.getRequestURI();
//        // 인증 불필요/토큰 발급 계열은 스킵
//        if ("/api/admin/login".equals(uri)) return true;
//        if ("/api/auth/refresh".equals(uri)) return true;
//        if ("/api/auth/csrf".equals(uri)) return true;
//        return false;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest req,
//                                    HttpServletResponse res,
//                                    FilterChain chain) throws ServletException, IOException {
//
//        // 1) 쿠키에서 AccessToken 추출 (쿠키명은 설정에서 온다)
//        String accessCookieName = cookieProps.getAccessName(); // 예: "AccessToken"
//        String token = resolveCookie(req, accessCookieName);
//
//        // 2) 유효 토큰이면 SecurityContext 채우기
//        if (token != null
//                && jwtTokenProvider.validateToken(token)
//                && SecurityContextHolder.getContext().getAuthentication() == null) {
//
//            Long userId    = jwtTokenProvider.getUserId(token);
//            String email   = jwtTokenProvider.getUsername(token);     // subject 등
//            String roleStr = jwtTokenProvider.getUserRole(token);     // 예: "ADMIN" 또는 "ADMIN,MANAGER"
//
//            // 권한 리스트 생성 (hasRole('X') ←→ ROLE_X 매칭)
//            List<SimpleGrantedAuthority> authorities =
//                    (roleStr == null || roleStr.isBlank())
//                            ? List.of(new SimpleGrantedAuthority("ROLE_USER")) // 기본 권한(선택)
//                            : Arrays.stream(roleStr.split(","))
//                            .map(String::trim)
//                            .filter(s -> !s.isEmpty())
//                            .map(r -> r.startsWith("ROLE_") ? r : "ROLE_" + r)
//                            .map(SimpleGrantedAuthority::new)
//                            .toList();
//
//            //principal 을 CustomUserDetails 로 설정 (컨트롤러에서 @AuthenticationPrincipal 로 주입 가능)
//            CustomUserDetails principal =
//                    new CustomUserDetails(userId, roleStr, email, /* userRegisterType */ null);
//
//            UsernamePasswordAuthenticationToken auth =
//                    new UsernamePasswordAuthenticationToken(principal, null, authorities);
//
//            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
//            SecurityContextHolder.getContext().setAuthentication(auth);
//        }
//
//        chain.doFilter(req, res);
//    }
//
//    private String resolveCookie(HttpServletRequest req, String name) {
//        Cookie[] cookies = req.getCookies();
//        if (cookies == null) return null;
//        for (Cookie c : cookies) {
//            if (name.equals(c.getName())) return c.getValue();
//        }
//        return null;
//    }
//}
