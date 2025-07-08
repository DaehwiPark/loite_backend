package com.boot.loiteBackend.web.social.client;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.social.config.OAuthProperties;
import com.boot.loiteBackend.web.social.dto.kakao.KakaoTokenResponseDto;
import com.boot.loiteBackend.web.social.dto.kakao.KakaoUserResponseDto;
import com.boot.loiteBackend.web.social.error.KakaoLoginErrorCode;
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

    // ========================
    // 인증 URL 생성
    // ========================

    // 로그인용 URL 생성
    public String getLoginUrl() {
        return buildAuthorizeUrl(oAuthProperties.getKakao().getRedirectUri());
    }

    // 연동용 URL 생성
    public String getLinkingUrl() {
        return buildAuthorizeUrl(oAuthProperties.getKakao().getLinkRedirectUri());
    }

    private String buildAuthorizeUrl(String redirectUri) {
        OAuthProperties.Provider kakao = oAuthProperties.getKakao();
        return kakao.getAuthEndpoint()
                + "?client_id=" + kakao.getClientId()
                + "&redirect_uri=" + redirectUri
                + "&response_type=" + kakao.getResponseType();
    }

    // ========================
    // 🔹 토큰 요청
    // ========================

    // 로그인용 액세스 토큰 요청
    public String requestAccessToken(String code) {
        return requestAccessTokenInternal(code, oAuthProperties.getKakao().getRedirectUri());
    }

    // 연동용 액세스 토큰 요청
    public String requestLinkingAccessToken(String code) {
        return requestAccessTokenInternal(code, oAuthProperties.getKakao().getLinkRedirectUri());
    }

    private String requestAccessTokenInternal(String code, String redirectUri) {
        OAuthProperties.Provider kakao = oAuthProperties.getKakao();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakao.getClientId());
        params.add("redirect_uri", redirectUri);
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

    // ========================
    // 🔹 사용자 정보 요청
    // ========================

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
