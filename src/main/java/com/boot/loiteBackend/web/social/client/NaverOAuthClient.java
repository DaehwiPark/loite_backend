package com.boot.loiteBackend.web.social.client;

import com.boot.loiteBackend.web.social.config.OAuthProperties;
import com.boot.loiteBackend.web.social.model.OAuthUserInfo;
import com.boot.loiteBackend.web.social.dto.naver.NaverTokenResponseDto;
import com.boot.loiteBackend.web.social.dto.naver.NaverUserResponseDto;
import com.boot.loiteBackend.web.social.error.NaverLoginErrorCode;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.social.model.NaverOAuthUserInfo;
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

    // 로그인용 URL
    public String getLoginUrl() {
        return buildAuthorizeUrl(oAuthProperties.getNaver().getRedirectUri());
    }

    // 연동용 URL
    public String getLinkingUrl() {
        return buildAuthorizeUrl(oAuthProperties.getNaver().getLinkRedirectUri());
    }

    // 인증용 URL (마이페이지 인증)

    public String getVerifyUrl() {
        return buildAuthorizeUrl(oAuthProperties.getNaver().getVerifyRedirectUri());
    }

    // 공통 URL 생성
    private String buildAuthorizeUrl(String redirectUri) {
        OAuthProperties.Provider naver = oAuthProperties.getNaver();
        return naver.getAuthEndpoint()
                + "?client_id=" + naver.getClientId()
                + "&redirect_uri=" + redirectUri
                + "&response_type=" + naver.getResponseType()
                + "&state=loite"; // CSRF 대응
    }

    // 로그인용 토큰
    public String requestAccessToken(String code) {
        return requestAccessToken(code, oAuthProperties.getNaver().getRedirectUri());
    }

    // 연동용 토큰
    public String requestLinkingAccessToken(String code) {
        return requestAccessToken(code, oAuthProperties.getNaver().getLinkRedirectUri());
    }

    // 인증용 토큰 (마이페이지 인증)
    public String requestVerifyAccessToken(String code) {
        return requestAccessToken(code, oAuthProperties.getNaver().getVerifyRedirectUri());
    }

    // 공통 액세스 토큰 요청 로직
    private String requestAccessToken(String code, String redirectUri) {
        OAuthProperties.Provider naver = oAuthProperties.getNaver();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", naver.getClientId());
        params.add("client_secret", naver.getClientSecret());
        params.add("redirect_uri", redirectUri);
        params.add("code", code);
        params.add("state", "loite");

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
            }
        } catch (HttpClientErrorException e) {
            log.error("네이버 토큰 요청 오류: {}", e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("네이버 토큰 요청 실패", e);
        }

        throw new CustomException(NaverLoginErrorCode.FAILED_TO_GET_TOKEN);
    }

    // 사용자 정보 요청
    public OAuthUserInfo requestUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        try {
            ResponseEntity<NaverUserResponseDto> response = restTemplate.exchange(
                    "https://openapi.naver.com/v1/nid/me",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    NaverUserResponseDto.class
            );

            NaverUserResponseDto user = response.getBody();
            log.debug("네이버 사용자 정보 응답: {}", user);

            return new NaverOAuthUserInfo(user);

        } catch (HttpClientErrorException e) {
            log.error("네이버 사용자 정보 요청 실패 (Client Error): {}", e.getResponseBodyAsString());
            throw new CustomException(NaverLoginErrorCode.FAILED_TO_GET_USER);
        } catch (Exception e) {
            log.error("네이버 사용자 정보 요청 실패", e);
            throw new CustomException(NaverLoginErrorCode.FAILED_TO_GET_USER);
        }
    }
}
