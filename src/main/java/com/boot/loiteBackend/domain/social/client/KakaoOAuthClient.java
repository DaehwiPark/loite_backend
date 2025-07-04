package com.boot.loiteBackend.domain.social.client;

import com.boot.loiteBackend.domain.social.config.OAuthProperties;
import com.boot.loiteBackend.domain.social.dto.kakao.KakaoTokenResponseDto;
import com.boot.loiteBackend.domain.social.dto.kakao.KakaoUserResponseDto;
import com.boot.loiteBackend.domain.social.error.KakaoLoginErrorCode;
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
public class KakaoOAuthClient {

    private final OAuthProperties oAuthProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    public String getLoginUrl() {
        OAuthProperties.Provider kakao = oAuthProperties.getKakao();
        return kakao.getAuthEndpoint()
                + "?client_id=" + kakao.getClientId()
                + "&redirect_uri=" + kakao.getRedirectUri()
                + "&response_type=" + kakao.getResponseType();
    }

    public String requestAccessToken(String code) {
        OAuthProperties.Provider kakao = oAuthProperties.getKakao();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakao.getClientId());
        params.add("redirect_uri", kakao.getRedirectUri());
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<KakaoTokenResponseDto> response = restTemplate.postForEntity(
                    kakao.getTokenEndpoint(), request, KakaoTokenResponseDto.class
            );
            KakaoTokenResponseDto body = response.getBody();
            log.debug("카카오 토큰 응답: {}", body);

            if (body != null && body.getAccessToken() != null) {
                return body.getAccessToken();
            }
        } catch (HttpClientErrorException e) {
            log.error("카카오 토큰 요청 오류: {}", e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("카카오 토큰 요청 실패", e);
        }

        throw new CustomException(KakaoLoginErrorCode.FAILED_TO_GET_TOKEN);
    }

    public KakaoUserResponseDto requestUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        try {
            ResponseEntity<KakaoUserResponseDto> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    KakaoUserResponseDto.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("카카오 사용자 정보 요청 실패", e);
            throw new CustomException(KakaoLoginErrorCode.FAILED_TO_GET_USER);
        }
    }
}
