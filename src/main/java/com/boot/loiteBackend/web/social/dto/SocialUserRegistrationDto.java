package com.boot.loiteBackend.web.social.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "소셜 사용자 회원가입 요청 DTO")
public class SocialUserRegistrationDto {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    @Schema(description = "사용자 이메일", example = "user@example.com", required = true)
    private String userEmail;

    @NotBlank(message = "이름은 필수입니다.")
    @Schema(description = "사용자 이름", example = "홍길동", required = true)
    private String userName;

    @JsonProperty("isOver14")
    @Schema(description = "만 14세 이상 여부", example = "true")
    private boolean isOver14;

    @JsonProperty("agreeTerms")
    @Schema(description = "이용약관 동의 여부", example = "true")
    private boolean agreeTerms;

    @JsonProperty("agreePrivacy")
    @Schema(description = "개인정보 처리방침 동의 여부", example = "true")
    private boolean agreePrivacy;

    @JsonProperty("agreeMarketingSns")
    @Schema(description = "마케팅 수신 동의 (SNS)", example = "false")
    private boolean agreeMarketingSns;

    @JsonProperty("agreeMarketingEmail")
    @Schema(description = "마케팅 수신 동의 (이메일)", example = "false")
    private boolean agreeMarketingEmail;

    @NotBlank(message = "소셜 ID는 필수입니다.")
    @Schema(description = "소셜 제공자 고유 식별자", example = "12345678901234567890", required = true)
    private String socialId;
}
