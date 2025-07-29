package com.boot.loiteBackend.web.user.general.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "비밀번호 변경 요청 DTO")
public class UpdatePasswordRequestDto {

    @Schema(description = "비밀번호를 변경할 계정의 이메일", example = "user@example.com", required = true)
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    @Schema(description = "새 비밀번호", example = "newSecureP@ssw0rd", required = true)
    @NotBlank(message = "새 비밀번호는 필수 입력값입니다.")
    private String newPassword;
}