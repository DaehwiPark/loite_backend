package com.boot.loiteBackend.web.social.login.service;

import com.boot.loiteBackend.global.error.exception.CustomException;
import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.web.login.dto.LoginResponseDto;
import com.boot.loiteBackend.web.social.entity.UserSocialEntity;
import com.boot.loiteBackend.web.social.login.config.KakaoOAuthProperties;
import com.boot.loiteBackend.web.social.login.dto.KakaoTokenResponseDto;
import com.boot.loiteBackend.web.social.login.dto.KakaoUserResponse;
import com.boot.loiteBackend.web.social.login.dto.SocialUserRegistrationDto;
import com.boot.loiteBackend.web.social.login.error.KakaoLoginErrorCode;
import com.boot.loiteBackend.web.auth.token.service.TokenService;
import com.boot.loiteBackend.web.social.repository.UserSocialRepository;
import com.boot.loiteBackend.web.user.entity.UserEntity;
import com.boot.loiteBackend.web.user.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoLoginServiceImpl implements KakaoLoginService {

    private final KakaoOAuthProperties kakaoOAuthProperties;
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserRepository userRepository;
    private final UserSocialRepository userSocialRepository;
    private final TokenService tokenService;

    @Override
    public String getKakaoLoginUrl() {
        try {
            return kakaoOAuthProperties.getAuthEndpoint()
                    + "?client_id=" + kakaoOAuthProperties.getClientId()
                    + "&redirect_uri=" + kakaoOAuthProperties.getRedirectUri()
                    + "&response_type=" + kakaoOAuthProperties.getResponseType();
        } catch (Exception e) {
            log.error("카카오 로그인 URL 생성 실패", e);
            throw new CustomException(KakaoLoginErrorCode.FAILED_TO_GET_AUTH_URL);
        }
    }

    @Override
    public ApiResponse<LoginResponseDto> kakaoLoginCallback(String code, HttpServletResponse response) {
        String accessToken = getKakaoAccessToken(code);
        KakaoUserResponse kakaoUser = getKakaoUserProfile(accessToken);

        if (kakaoUser == null || kakaoUser.getId() == null || kakaoUser.getKakao_account() == null) {
            return ApiResponse.error(KakaoLoginErrorCode.FAILED_TO_GET_USER);
        }

        String kakaoId = kakaoUser.getId().toString();
        String email = kakaoUser.getKakao_account().getEmail();
        String name = kakaoUser.getKakao_account().getProfile().getNickname();

        Optional<UserEntity> optionalUser = userRepository.findByUserEmail(email);
        if (optionalUser.isPresent()) {
            UserEntity user = optionalUser.get();

            boolean isLinkedToKakao = user.getUserSocials().stream()
                    .anyMatch(social -> "KAKAO".equalsIgnoreCase(social.getSocialType())
                            && kakaoId.equals(social.getSocialNumber()));

            if (isLinkedToKakao) {
                LoginResponseDto loginDto = tokenService.getLoginToken(user, response);
                return ApiResponse.ok(loginDto, "카카오 로그인 성공");
            } else {
                return ApiResponse.error(KakaoLoginErrorCode.ALREADY_REGISTERED_WITH_OTHER_PROVIDER);
            }
        }
        // 신규 가입 유도
        log.info("카카오 회원가입 유도 - 이메일: {}, 소셜ID: {}", email, kakaoId);
        return ApiResponse.<LoginResponseDto>builder()
                .success(false)
                .message(KakaoLoginErrorCode.USER_NOT_REGISTERED.getMessage())
                .code(KakaoLoginErrorCode.USER_NOT_REGISTERED.getCode())
                .data(null)
                .extra(Map.of(
                        "email", email,
                        "socialId", kakaoId,
                        "name", name,
                        "code", code
                ))
                .build();
    }

    private String getKakaoAccessToken(String code) {
        log.debug("받은 authorization_code: {}", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoOAuthProperties.getClientId());
        params.add("redirect_uri", kakaoOAuthProperties.getRedirectUri());
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<KakaoTokenResponseDto> response = restTemplate.postForEntity(
                    kakaoOAuthProperties.getTokenEndpoint(),
                    request,
                    KakaoTokenResponseDto.class
            );

            KakaoTokenResponseDto body = response.getBody();
            log.debug("카카오 토큰 응답: {}", body);

            if (body != null && body.getAccess_token() != null) {
                return body.getAccess_token();
            } else {
                log.error("access_token 없음: {}", body);
                throw new CustomException(KakaoLoginErrorCode.FAILED_TO_GET_TOKEN);
            }
        } catch (HttpClientErrorException e) {
            log.error("카카오 토큰 요청 오류 응답: {}", e.getResponseBodyAsString());
            throw new CustomException(KakaoLoginErrorCode.FAILED_TO_GET_TOKEN);
        } catch (Exception e) {
            log.error("카카오 토큰 요청 실패", e);
            throw new CustomException(KakaoLoginErrorCode.FAILED_TO_GET_TOKEN);
        }
    }

    private KakaoUserResponse getKakaoUserProfile(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<?> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    entity,
                    KakaoUserResponse.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("카카오 사용자 정보 요청 실패", e);
            throw new CustomException(KakaoLoginErrorCode.FAILED_TO_GET_USER);
        }
    }

    @Override
    @Transactional
    public LoginResponseDto registerKakaoUser(SocialUserRegistrationDto dto, HttpServletResponse response) {
        try {
            if (userRepository.existsByUserEmail(dto.getUserEmail())) {
                throw new CustomException(KakaoLoginErrorCode.ALREADY_REGISTERED_WITH_OTHER_PROVIDER);
            }

            UserEntity user = UserEntity.builder()
                    .userEmail(dto.getUserEmail())
                    .userName(dto.getUserName())
                    .isOver14(dto.isOver14())
                    .agreeTerms(dto.isAgreeTerms())
                    .agreePrivacy(dto.isAgreePrivacy())
                    .agreeMarketingSns(dto.isAgreeMarketingSns())
                    .agreeMarketingEmail(dto.isAgreeMarketingEmail())
                    .emailVerified(true)
                    .emailVerifiedAt(LocalDateTime.now())
                    .role("USER")
                    .userStatus("ACTIVE")
                    .build();

            userRepository.save(user);

            UserSocialEntity social = UserSocialEntity.builder()
                    .user(user)
                    .socialType("KAKAO")
                    .socialNumber(dto.getSocialId())
                    .connectedAt(LocalDateTime.now())
                    .build();

            userSocialRepository.save(social);

            return tokenService.getLoginToken(user, response);

        } catch (Exception e) {
            log.error("카카오 사용자 등록 실패", e);
            throw new CustomException(KakaoLoginErrorCode.FAILED_TO_REGISTER_USER);
        }
    }

    @Override
    public KakaoUserResponse getKakaoUserInfo(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<KakaoUserResponse> userInfoResponse = restTemplate.exchange(
                    "https://kapi.kakao.com/v2/user/me",
                    HttpMethod.GET,
                    entity,
                    KakaoUserResponse.class
            );

            return userInfoResponse.getBody();
        } catch (Exception e) {
            log.error("카카오 사용자 정보 조회 실패", e);
            throw new CustomException(KakaoLoginErrorCode.FAILED_TO_GET_USER);
        }
    }
}
