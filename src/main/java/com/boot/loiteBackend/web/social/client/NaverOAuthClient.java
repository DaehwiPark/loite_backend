package com.boot.loiteBackend.web.social.client;

import com.boot.loiteBackend.web.social.config.OAuthProperties;
import com.boot.loiteBackend.web.social.dto.naver.NaverTokenResponseDto;
import com.boot.loiteBackend.web.social.dto.naver.NaverUserResponseDto;
import com.boot.loiteBackend.web.social.error.NaverLoginErrorCode;
import com.boot.loiteBackend.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
@Slf4j
public class NaverOAuthClient {

    private final OAuthProperties oAuthProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    public String requestAccessToken(String code) {
        OAuthProperties.Provider naver = oAuthProperties.getNaver();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", naver.getClientId());
        params.add("client_secret", naver.getClientSecret());
        params.add("redirect_uri", naver.getRedirectUri());
        params.add("code", code);
        params.add("state", "loite"); // CSRF 대응용 임의의 값

        HttpEntity<?> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<NaverTokenResponseDto> response = restTemplate.postForEntity(
                    naver.getTokenEndpoint(),
                    request,
                    NaverTokenResponseDto.class
            );

            NaverTokenResponseDto body = response.getBody();
            log.debug("네이버 토큰 응답: {}", body);

            if (body != null && body.getAccessToken() != null) {
                return body.getAccessToken();
            } else {
                throw new CustomException(NaverLoginErrorCode.FAILED_TO_GET_TOKEN);
            }

        } catch (HttpClientErrorException e) {
            log.error("네이버 토큰 요청 실패 (Client Error): {}", e.getResponseBodyAsString());
            throw new CustomException(NaverLoginErrorCode.FAILED_TO_GET_TOKEN);
        } catch (Exception e) {
            log.error("네이버 토큰 요청 실패", e);
            throw new CustomException(NaverLoginErrorCode.FAILED_TO_GET_TOKEN);
        }
    }

    public NaverUserResponseDto requestUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        try {
            ResponseEntity<NaverUserResponseDto> response = restTemplate.exchange(
                    "https://openapi.naver.com/v1/nid/me",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    NaverUserResponseDto.class
            );

            log.debug("네이버 사용자 정보 응답: {}", response.getBody());
            return response.getBody();

        } catch (HttpClientErrorException e) {
            log.error("네이버 사용자 정보 요청 실패 (Client Error): {}", e.getResponseBodyAsString());
            throw new CustomException(NaverLoginErrorCode.FAILED_TO_GET_USER);
        } catch (Exception e) {
            log.error("네이버 사용자 정보 요청 실패", e);
            throw new CustomException(NaverLoginErrorCode.FAILED_TO_GET_USER);
        }
    }

    public String getLoginUrl() {
        OAuthProperties.Provider naver = oAuthProperties.getNaver();

        return naver.getAuthEndpoint()
                + "?client_id=" + naver.getClientId()
                + "&redirect_uri=" + naver.getRedirectUri()
                + "&response_type=" + naver.getResponseType()
                + "&state=loite";
    }
}
