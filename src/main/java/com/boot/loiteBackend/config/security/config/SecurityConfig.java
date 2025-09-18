package com.boot.loiteBackend.config.security.config;

import com.boot.loiteBackend.config.security.filter.JwtAuthenticationFilter;
import com.boot.loiteBackend.config.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // CORS / CSRF / 세션 비활성화(Stateless)
                .cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 권한 매칭
                .authorizeHttpRequests(auth -> auth
                        // 프리플라이트 허용
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // 문서/정적 리소스(스웨거 등) - 필요 시 운영에서 닫으세요.
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()

                        // 업로드 파일 열람만 허용 (GET/HEAD)
                        .requestMatchers(HttpMethod.GET, "/uploads/**").permitAll()
                        .requestMatchers(HttpMethod.HEAD, "/uploads/**").permitAll()

                        // 공개 API
                        .requestMatchers("/api/public/**", "/api/auth/**").permitAll()

                        // 일반 사용자 전용 API(로그인 필요)
                        .requestMatchers("/api/private/**").authenticated()

                        // 관리자 전용 API
                        .requestMatchers("/api/admin/**").permitAll() //.hasRole("ADMIN")
                        .requestMatchers("/api/admin/manager/**").permitAll() //.hasRole("MANAGER")
                        .requestMatchers("/api/admin/notices").permitAll() //.hasAnyRole("ADMIN", "MANAGER")

                        // 그 외 전부 인증 필요
                        .anyRequest().authenticated()
                )

                // JWT 필터
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * CORS 설정
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);

        // loite.co.kr 도메인 및 로컬 개발 오리진
        config.setAllowedOriginPatterns(List.of(
                "https://loite.co.kr",
                "https://www.loite.co.kr",
                "https://admin.loite.co.kr",
                "https://*.loite.co.kr",
                "http://localhost:5173",
                "http://localhost:3000"
        ));

        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS","HEAD"));
        config.setAllowedHeaders(List.of("*"));
        config.setExposedHeaders(List.of("Set-Cookie","Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}