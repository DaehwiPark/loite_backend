package com.boot.loiteBackend.web.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "회원가입 요청 DTO")
public class UserCreateRequestDto {

    @Schema(description = "이메일", example = "user@example.com")
    @Email
    @NotBlank
    private String userEmail;

    @Schema(description = "비밀번호", example = "password1234")
    @NotBlank
    private String userPassword;

    @Schema(description = "비밀번호 확인", example = "password1234")
    @NotBlank
    private String userPasswordCheck;

    @Schema(description = "이름", example = "홍길동")
    @NotBlank
    private String userName;

    @Schema(description = "휴대폰 번호", example = "010-1234-5678")
    @NotBlank
    private String userPhone;

    @Schema(description = "생년월일", example = "1990-01-01")
    @NotBlank
    private String userBirthdate;

    @Schema(description = "만 14세 이상 여부", example = "true")
    @AssertTrue(message = "만 14세 이상 동의는 필수입니다.")
    private Boolean isOver14;

    @Schema(description = "이용약관 동의 여부", example = "true")
    @AssertTrue(message = "이용약관 동의는 필수입니다.")
    private Boolean agreeTerms;

    @Schema(description = "개인정보 수집 및 이용 동의 여부", example = "true")
    @AssertTrue(message = "개인정보 수집 및 이용 동의는 필수입니다.")
    private Boolean agreePrivacy;

    @Schema(description = "광고/마케팅 수신 동의 여부", example = "false")
    private Boolean agreeMarketing;
}
