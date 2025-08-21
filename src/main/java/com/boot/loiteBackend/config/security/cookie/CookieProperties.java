package com.boot.loiteBackend.config.security.cookie;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security.cookie")
public class CookieProperties {

    /** HTTPS 전용 쿠키 여부 (운영: true, 로컬: false) */
    private boolean secure = true;

    /** SameSite: Lax | Strict | None  */
    private String sameSite = "Lax";

    /** 쿠키 도메인 (예: .loite.co.kr). 필요 없으면 비움 */
    private String domain;

    /** 쿠키 path */
    private String path = "/";

    /** 액세스 토큰 쿠키명 */
    private String accessName = "AccessToken";

    /** 리프레시 토큰 쿠키명 */
    private String refreshName = "RefreshToken";
}