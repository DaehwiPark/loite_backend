package com.boot.loiteBackend.web.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "로그인 요청 DTO (이메일/비밀번호 기반 로그인)")
public class LoginRequestDto {

    @Schema(description = "사용자 이메일 주소", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @Schema(description = "사용자 비밀번호", example = "your_password123!", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
}
