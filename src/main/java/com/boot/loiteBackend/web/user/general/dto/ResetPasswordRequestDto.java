package com.boot.loiteBackend.web.user.general.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "비밀번호 찾기 조회 DTO")
public class ResetPasswordRequestDto {

    @Schema(description = "회원가입 시 등록한 이메일", example = "user@example.com", required = true)
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    private String email;

    @Schema(description = "회원가입 시 등록한 이름", example = "홍길동", required = true)
    @NotBlank(message = "이름은 필수 입력값입니다.")
    private String name;

    @Schema(description = "회원가입 시 등록한 휴대폰 번호", example = "01012345678", required = true)
    @NotBlank(message = "휴대폰 번호는 필수 입력값입니다.")
    private String phone;
}