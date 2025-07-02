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

    private final KakaoOAuthProperties kakaoProps;
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserRepository userRepository;
    private final UserSocialRepository userSocialRepository;
    private final TokenService tokenService;

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

    public ApiResponse<LoginResponseDto> kakaoLoginCallback(String code, HttpServletResponse response) {
        String accessToken = getKakaoAccessToken(code);
        KakaoUserResponse kakaoUser = getKakaoUserProfile(accessToken);

        if (kakaoUser == null || kakaoUser.getId() == null || kakaoUser.getKakao_account() == null) {
            return ResponseEntity
                    .status(KakaoLoginErrorCode.FAILED_TO_GET_USER.getStatus())
                    .body(ApiResponse.<LoginResponseDto>builder()
                            .success(false)
                            .message(KakaoLoginErrorCode.FAILED_TO_GET_USER.getMessage())
                            .data(null)
                            .build()).getBody();
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
                return ResponseEntity.ok(ApiResponse.<LoginResponseDto>builder()
                        .success(true)
                        .message("카카오 로그인 성공")
                        .data(loginDto)
                        .build()).getBody();
            } else {
                return ResponseEntity
                        .status(KakaoLoginErrorCode.ALREADY_REGISTERED_WITH_OTHER_PROVIDER.getStatus())
                        .body(ApiResponse.<LoginResponseDto>builder()
                                .success(false)
                                .message(KakaoLoginErrorCode.ALREADY_REGISTERED_WITH_OTHER_PROVIDER.getMessage())
                                .data(null)
                                .build()).getBody();
            }
        }

        // 회원가입 유도 응답
        log.info("카카오 회원가입 유도 - 이메일: {}, 소셜ID: {}", email, kakaoId);
        return ResponseEntity
                .status(KakaoLoginErrorCode.USER_NOT_REGISTERED.getStatus())
                .body(ApiResponse.<LoginResponseDto>builder()
                        .success(false)
                        .message(KakaoLoginErrorCode.USER_NOT_REGISTERED.getMessage())
                        .data(null)
                        .extra(Map.of( // 필요 시 추가 데이터 전송
                                "email", email,
                                "socialId", kakaoId,
                                "name", name,
                                "code", code
                        ))
                        .build()).getBody();
    }


    private String getKakaoAccessToken(String code) {
        log.debug("받은 authorization_code: {}", code);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProps.getClientId());
        params.add("redirect_uri", kakaoProps.getRedirectUri());
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<KakaoTokenResponseDto> response = restTemplate.postForEntity(
                    kakaoProps.getTokenEndpoint(),
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
                    .agreeMarketing(dto.isAgreeMarketing())
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
