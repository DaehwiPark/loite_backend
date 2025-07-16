package com.boot.loiteBackend.web.social.handler;

import com.boot.loiteBackend.web.social.config.OAuthProperties;
import com.boot.loiteBackend.web.social.enums.ProviderType;
import io.lettuce.core.dynamic.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
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
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new RuntimeException("연동 해제 실패: " + response.getBody());
        }
    }
}
