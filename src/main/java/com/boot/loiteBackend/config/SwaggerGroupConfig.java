package com.boot.loiteBackend.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerGroupConfig {

    // 관리자 API 문서 그룹
    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("admin")
                .pathsToMatch("/api/admin/**")
                .build();
    }

    // 쇼핑몰(사용자) API 문서 그룹
    @Bean
    public GroupedOpenApi webApi() {
        return GroupedOpenApi.builder()
                .group("web")
                .pathsToMatch("/api/private/**", "/api/public/**")
                .build();
    }

    // 인증 API 문서 그룹
    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder()
                .group("auth")
                .pathsToMatch("/api/auth/**")
                .build();
    }
}
