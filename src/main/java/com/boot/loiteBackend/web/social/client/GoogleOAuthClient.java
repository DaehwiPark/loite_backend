package com.boot.loiteBackend.web.social.client;

import com.boot.loiteBackend.web.social.config.OAuthProperties;
import com.boot.loiteBackend.web.social.model.OAuthUserInfo;
import com.boot.loiteBackend.web.social.dto.google.GoogleTokenResponseDto;
import com.boot.loiteBackend.web.social.dto.google.GoogleUserResponseDto;
import com.boot.loiteBackend.web.social.error.GoogleLoginErrorCode;
import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.social.model.GoogleOAuthUserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class GoogleOAuthClient {

    private final OAuthProperties oAuthProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    // 로그인용 URL
    public String getLoginUrl() {
        return buildAuthorizeUrl(oAuthProperties.getGoogle().getRedirectUri());
    }

    // 연동용 URL
    public String getLinkingUrl() {
        return buildAuthorizeUrl(oAuthProperties.getGoogle().getLinkRedirectUri());
    }

    // 인증용 URL (마이페이지 진입 등)
    public String getVerifyUrl() {
        return buildAuthorizeUrl(oAuthProperties.getGoogle().getVerifyRedirectUri());
    }

    // 공통 authorize URL 생성 로직
    private String buildAuthorizeUrl(String redirectUri) {
        OAuthProperties.Provider google = oAuthProperties.getGoogle();
        return google.getAuthEndpoint()
                + "?client_id=" + google.getClientId()
                + "&redirect_uri=" + URLEncoder.encode(redirectUri, StandardCharsets.UTF_8)
                + "&response_type=code"
                + "&scope=" + URLEncoder.encode(google.getScope(), StandardCharsets.UTF_8)
                + "&access_type=offline"
                + "&include_granted_scopes=true"
                + "&prompt=consent";
    }

    // 로그인용 토큰 요청
    public String requestAccessToken(String code) {
        return requestAccessTokenInternal(code, oAuthProperties.getGoogle().getRedirectUri());
    }

    // 연동용 토큰 요청
    public String requestLinkingAccessToken(String code) {
        return requestAccessTokenInternal(code, oAuthProperties.getGoogle().getLinkRedirectUri());
    }

    // 인증용 토큰 요청 (verify)
    public String requestVerifyAccessToken(String code) {
        return requestAccessTokenInternal(code, oAuthProperties.getGoogle().getVerifyRedirectUri());
    }

    // 내부 공통 액세스 토큰 요청
    private String requestAccessTokenInternal(String code, String redirectUri) {
        OAuthProperties.Provider google = oAuthProperties.getGoogle();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", google.getClientId());
        params.add("client_secret", google.getClientSecret());
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpEntity<?> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<GoogleTokenResponseDto> response = restTemplate.postForEntity(
                    google.getTokenEndpoint(),
                    request,
                    GoogleTokenResponseDto.class
            );
            GoogleTokenResponseDto body = response.getBody();
            log.debug("구글 토큰 응답: {}", body);

            if (body != null && body.getAccessToken() != null) {
                return body.getAccessToken();
            }
        } catch (HttpClientErrorException e) {
            log.error("구글 토큰 요청 오류: {}", e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("구글 토큰 요청 실패", e);
        }

        throw new CustomException(GoogleLoginErrorCode.FAILED_TO_GET_TOKEN);
    }

    // 사용자 정보 요청
    public OAuthUserInfo requestUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        try {
            ResponseEntity<GoogleUserResponseDto> response = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v3/userinfo",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    GoogleUserResponseDto.class
            );

            GoogleUserResponseDto googleUser = response.getBody();
            log.debug("구글 사용자 정보 응답: {}", googleUser);

            if (googleUser == null || googleUser.getSub() == null) {
                throw new CustomException(GoogleLoginErrorCode.FAILED_TO_GET_USER);
            }

            return new GoogleOAuthUserInfo(googleUser);

        } catch (HttpClientErrorException e) {
            log.error("구글 사용자 정보 요청 실패 (Client Error): {}", e.getResponseBodyAsString());
            throw new CustomException(GoogleLoginErrorCode.FAILED_TO_GET_USER);
        } catch (Exception e) {
            log.error("구글 사용자 정보 요청 실패", e);
            throw new CustomException(GoogleLoginErrorCode.FAILED_TO_GET_USER);
        }
    }
}

