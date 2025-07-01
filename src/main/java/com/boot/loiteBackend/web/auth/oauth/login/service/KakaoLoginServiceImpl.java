package com.boot.loiteBackend.web.auth.oauth.login.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.web.auth.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.auth.oauth.login.config.KakaoOAuthProperties;
import com.boot.loiteBackend.web.auth.oauth.login.error.KakaoLoginErrorCode;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoLoginServiceImpl implements KakaoLoginService {

    private final KakaoOAuthProperties kakaoProps;
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String getKakaoLoginUrl() {
        try {
            return kakaoProps.getAuthEndpoint()
                    + "?client_id=" + kakaoProps.getClientId()
                    + "&redirect_uri=" + kakaoProps.getRedirectUri()
                    + "&response_type=" + kakaoProps.getResponseType();
        } catch (Exception e) {
            log.error("카카오 로그인 URL 생성 실패", e);
            throw new CustomException(KakaoLoginErrorCode.FAILED_TO_GET_AUTH_URL);
        }
    }

//    @Override
//    public LoginResponseDto kakaoLoginCallback(String code, HttpServletResponse response) {
//        // 1. 토큰 요청
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        Map<String, String> params = new HashMap<>();
//        params.put("grant_type", "authorization_code");
//        params.put("client_id", kakaoProps.getClientId());
//        params.put("redirect_uri", kakaoProps.getRedirectUri());
//        params.put("code", code);
//
//        HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);
//        ResponseEntity<Map> tokenResponse;
//
//        try {
//            tokenResponse = restTemplate.postForEntity(
//                    kakaoProps.getTokenEndpoint(), request, Map.class);
//        } catch (Exception e) {
//            log.error("카카오 토큰 요청 실패", e);
//            throw new CustomException(KakaoLoginErrorCode.FAILED_TO_GET_TOKEN);
//        }
//
//        Map<String, Object> body = tokenResponse.getBody();
//        if (body == null || !body.containsKey("access_token")) {
//            throw new CustomException(KakaoLoginErrorCode.FAILED_TO_GET_TOKEN);
//        }
//
//        String accessToken = (String) body.get("access_token");
//
//        // 2. 사용자 정보 요청
//        Object kakaoUser = getKakaoUserInfo(accessToken);
//        if (kakaoUser == null) {
//            throw new CustomException(KakaoLoginErrorCode.FAILED_TO_GET_USER);
//        }
//
//        // 3. DB 사용자 처리 생략
//
//        // 4. JWT 발급 (임시)
//        return new LoginResponseDto("sample-jwt-access-token", "sample-refresh-token");
//    }
//
//    @Override
//    public LoginResponseDto registerKakaoUser(SocialUserRegistrationDto registrationDto, HttpServletResponse response) {
//        try {
//            // DB 저장 로직 생략
//            return new LoginResponseDto("sample-access-token", "sample-refresh-token");
//        } catch (Exception e) {
//            log.error("카카오 사용자 등록 실패", e);
//            throw new CustomException(KakaoLoginErrorCode.FAILED_TO_REGISTER_USER);
//        }
//    }
//
//    @Override
//    public Object getKakaoUserInfo(String accessToken) {
//        try {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setBearerAuth(accessToken);
//            HttpEntity<?> entity = new HttpEntity<>(headers);
//
//            ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
//                    "https://kapi.kakao.com/v2/user/me",
//                    HttpMethod.GET,
//                    entity,
//                    Map.class
//            );
//
//            return userInfoResponse.getBody();
//        } catch (Exception e) {
//            log.error("카카오 사용자 정보 조회 실패", e);
//            throw new CustomException(KakaoLoginErrorCode.FAILED_TO_GET_USER);
//        }
//    }
}
