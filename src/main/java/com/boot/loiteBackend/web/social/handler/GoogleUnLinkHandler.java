package com.boot.loiteBackend.web.social.handler;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.social.enums.ProviderType;
import com.boot.loiteBackend.web.social.error.SocialErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleUnLinkHandler implements OAuthUnLinkHandlers {

    @Override
    public ProviderType getProvider() {
        return ProviderType.GOOGLE;
    }

    @Override
    public void unlinkSocialAccount(String accessToken, String email) {
        String url = "https://oauth2.googleapis.com/revoke?token=" + accessToken;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<?> request = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<String> response;

        try {
            // INVALID_PROVIDER실제 구글 API에 revoke 요청 (POST)
            response = restTemplate.postForEntity(url, request, String.class);
        } catch (Exception e) {
            // INVALID_PROVIDER네트워크 오류, DNS 실패, 타임아웃 등 RestTemplate 자체 오류
            log.warn("구글 API 연동 해제 요청 실패: {}", e.getMessage(), e);
            throw new CustomException(SocialErrorCode.UNLINK_FAILED); // 500 INTERNAL_SERVER_ERROR
        }

        // INVALID_PROVIDER요청은 성공했으나 HTTP 응답 코드가 2xx가 아님 (실패 처리)
        if (!response.getStatusCode().is2xxSuccessful()) {
            log.warn("구글 연동 해제 실패. statusCode={}, body={}", response.getStatusCode(), response.getBody());

            if (response.getStatusCode().is4xxClientError()) {
                // INVALID_PROVIDER400번대 오류: 토큰 만료, 유효하지 않음, 이미 해제된 경우 등
                // => 인증 실패로 간주 (401 UNAUTHORIZED)
                throw new CustomException(SocialErrorCode.SOCIAL_VERIFICATION_FAILED);
            }

            // INVALID_PROVIDER500번대 오류 등 기타 예외 → 시스템적 문제로 간주
            throw new CustomException(SocialErrorCode.UNLINK_FAILED); // 500 INTERNAL_SERVER_ERROR
        }

        // INVALID_PROVIDER연동 해제 성공 (2xx 응답)
        log.info("구글 연동 해제 성공. email={}, token={}", email, accessToken);
    }
}