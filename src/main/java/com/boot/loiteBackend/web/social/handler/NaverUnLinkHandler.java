package com.boot.loiteBackend.web.social.handler;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.social.config.OAuthProperties;
import com.boot.loiteBackend.web.social.enums.ProviderType;
import com.boot.loiteBackend.web.social.error.SocialErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class NaverUnLinkHandler implements OAuthUnLinkHandlers {

    private final OAuthProperties oAuthProperties;

    @Override
    public ProviderType getProvider() {
        return ProviderType.NAVER;
    }

    @Override
    public void unlinkSocialAccount(String accessToken, String email) {
        OAuthProperties.Provider naver = oAuthProperties.getNaver();

        String url = UriComponentsBuilder.fromHttpUrl("https://nid.naver.com/oauth2.0/token")
                .queryParam("grant_type", "delete")
                .queryParam("client_id", naver.getClientId())
                .queryParam("client_secret", naver.getClientSecret())
                .queryParam("access_token", accessToken)
                .queryParam("service_provider", "NAVER")
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;

        try {
            // 네이버 API에 unlink 요청 (GET)
            response = restTemplate.getForEntity(url, String.class);
        } catch (Exception e) {
            // RestTemplate에서 발생하는 예외 (네트워크, 타임아웃 등)
            log.warn("네이버 API 연동 해제 요청 실패: {}", e.getMessage(), e);
            throw new CustomException(SocialErrorCode.UNLINK_FAILED);
        }

        if (!response.getStatusCode().is2xxSuccessful()) {
            // HTTP 응답은 왔으나 실패 상태일 때 (ex. 400/401/500 등)
            log.warn("네이버 연동 해제 실패. statusCode={}, body={}", response.getStatusCode(), response.getBody());

            if (response.getStatusCode().is4xxClientError()) {
                // 클라이언트 오류 → 인증 실패로 간주
                throw new CustomException(SocialErrorCode.SOCIAL_VERIFICATION_FAILED);
            }

            // 서버 오류 등 기타 상황
            throw new CustomException(SocialErrorCode.UNLINK_FAILED);
        }

        // 성공 로그
        log.info("네이버 연동 해제 성공. email={}, token={}", email, accessToken);
    }
}