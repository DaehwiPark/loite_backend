package com.boot.loiteBackend.domain.social.dto.naver;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "네이버 사용자 정보 응답 DTO")
public class NaverUserResponseDto {

    @Schema(description = "사용자 정보 객체")
    private NaverUser response;

    @Getter
    @Setter
    @Schema(description = "네이버 사용자 상세 정보")
    public static class NaverUser {

        @Schema(description = "사용자 고유 ID", example = "shf28d29d2df")
        private String id;

        @Schema(description = "이메일 주소", example = "user@naver.com")
        private String email;

        @Schema(description = "이름", example = "홍길동")
        private String name;
    }
}
