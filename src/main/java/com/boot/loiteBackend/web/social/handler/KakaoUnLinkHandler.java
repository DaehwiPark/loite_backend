package com.boot.loiteBackend.web.social.handler;

import com.boot.loiteBackend.web.social.config.OAuthProperties;
import com.boot.loiteBackend.web.social.enums.ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
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
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(null, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("카카오 연동 해제 실패: " + response.getBody());
        }
    }

}
