package com.boot.loiteBackend.web.social.dto.naver;

import com.boot.loiteBackend.web.social.link.model.OAuthUserInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 네이버 사용자 정보 응답 DTO (OAuthUserInfo 인터페이스 구현)
 */
@Getter
@Setter
@Schema(description = "네이버 사용자 정보 응답 DTO")
public class NaverUserResponseDto implements OAuthUserInfo {

    @Schema(description = "네이버 사용자 정보(response)")
    private NaverAccount response;

    @Override
    public String getSocialId() {
        return response != null ? response.getId() : null;
    }

    @Override
    public String getEmail() {
        return response != null ? response.getEmail() : null;
    }

    @Override
    public String getName() {
        return response != null ? response.getName() : null;
    }


    @Override
    public String getProvider() {
        return "NAVER";
    }

    @Getter
    @Setter
    @Schema(description = "네이버 사용자 상세 정보")
    public static class NaverAccount {

        @Schema(description = "네이버 고유 ID", example = "shf28d29d2df")
        private String id;

        @Schema(description = "이메일 주소", example = "user@naver.com")
        private String email;

        @Schema(description = "이름", example = "홍길동")
        private String name;
    }
}
