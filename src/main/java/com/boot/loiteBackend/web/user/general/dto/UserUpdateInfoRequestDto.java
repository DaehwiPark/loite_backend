package com.boot.loiteBackend.web.user.general.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "회원 기본 정보 수정 요청 DTO")
public class UserUpdateInfoRequestDto {

    @Schema(description = "변경할 휴대폰 번호", example = "01012345678", requiredMode = Schema.RequiredMode.REQUIRED)
    private String phone;

    @Schema(description = "생년월일 (yyyy-MM-dd 형식)", example = "1990-05-15", requiredMode = Schema.RequiredMode.REQUIRED)
    private String birthdate;
}