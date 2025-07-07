package com.boot.loiteBackend.domain.social.client;

import com.boot.loiteBackend.domain.social.config.OAuthProperties;
import com.boot.loiteBackend.domain.social.dto.google.GoogleTokenResponseDto;
import com.boot.loiteBackend.domain.social.dto.google.GoogleUserResponseDto;
import com.boot.loiteBackend.domain.social.error.GoogleLoginErrorCode;
import com.boot.loiteBackend.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthClient {

    private final OAuthProperties oAuthProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    public String buildLoginUrl() {
        OAuthProperties.Provider google = oAuthProperties.getGoogle();
        return google.getAuthEndpoint()
                + "?client_id=" + google.getClientId()
                + "&redirect_uri=" + google.getRedirectUri()
                + "&response_type=code"
                + "&scope=" + URLEncoder.encode(google.getScope(), StandardCharsets.UTF_8)
                + "&access_type=offline"
                + "&include_granted_scopes=true"
                + "&prompt=consent";
    }

    public String requestAccessToken(String code) {
        OAuthProperties.Provider google = oAuthProperties.getGoogle();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", google.getClientId());
        params.add("client_secret", google.getClientSecret());
        params.add("redirect_uri", google.getRedirectUri());
        params.add("grant_type", "authorization_code");

        try {
            ResponseEntity<GoogleTokenResponseDto> response = restTemplate.postForEntity(
                    google.getTokenEndpoint(), new HttpEntity<>(params, headers), GoogleTokenResponseDto.class
            );
            return response.getBody().getAccessToken();
        } catch (Exception e) {
            log.error("구글 토큰 요청 실패", e);
            throw new CustomException(GoogleLoginErrorCode.FAILED_TO_GET_TOKEN);
        }
    }

    public GoogleUserResponseDto requestUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        try {
            ResponseEntity<GoogleUserResponseDto> response = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v3/userinfo",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    GoogleUserResponseDto.class
            );
            return response.getBody();
        } catch (Exception e) {
            log.error("구글 사용자 정보 요청 실패", e);
            throw new CustomException(GoogleLoginErrorCode.FAILED_TO_GET_USER);
        }
    }
}
