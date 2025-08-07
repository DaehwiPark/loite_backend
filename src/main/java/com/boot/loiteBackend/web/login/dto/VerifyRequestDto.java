package com.boot.loiteBackend.web.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "비밀번호 인증 요청 DTO (마이페이지 접근 등 민감한 기능 접근 시 사용)")
public class VerifyRequestDto {

    @Schema(description = "비밀번호", example = "1234abcd!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
