package com.boot.loiteBackend.web.social.handler;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.social.config.OAuthProperties;
import com.boot.loiteBackend.web.social.enums.ProviderType;
import com.boot.loiteBackend.web.social.error.SocialErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class KakaoUnLinkHandler implements OAuthUnLinkHandlers {

    private final OAuthProperties oAuthProperties;

    @Override
    public ProviderType getProvider() {
        return ProviderType.KAKAO;
    }

    @Override
    public void unlinkSocialAccount(String accessToken, String unusedEmail) {
        String url = "https://kapi.kakao.com/v1/user/unlink";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken); // 인증 토큰 전달
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(null, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;

        try {
            // Kakao unlink 요청
            response = restTemplate.postForEntity(url, entity, String.class);
        } catch (Exception e) {
            // RestTemplate 자체에서 발생하는 예외 (네트워크 장애, 연결 실패 등)
            log.warn("카카오 API 연동 해제 요청 실패: {}", e.getMessage(), e);
            throw new CustomException(SocialErrorCode.UNLINK_FAILED);
        }

        if (!response.getStatusCode().is2xxSuccessful()) {
            // 요청은 되었지만 실패 응답이 내려온 경우
            log.warn("카카오 연동 해제 실패. statusCode={}, body={}", response.getStatusCode(), response.getBody());

            if (response.getStatusCode().is4xxClientError()) {
                // 클라이언트 오류 → 토큰이 만료되었거나 잘못되었거나 이미 연동 해제됨
                throw new CustomException(SocialErrorCode.SOCIAL_VERIFICATION_FAILED);
            }

            // 그 외 서버 오류
            throw new CustomException(SocialErrorCode.UNLINK_FAILED);
        }

        // 성공 로그
        log.info("카카오 연동 해제 성공. token={}", accessToken);
    }
}