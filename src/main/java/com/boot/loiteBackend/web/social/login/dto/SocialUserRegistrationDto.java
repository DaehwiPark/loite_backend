package com.boot.loiteBackend.web.social.login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SocialUserRegistrationDto {

    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "이메일 형식이 올바르지 않습니다.")
    private String userEmail;

    @NotBlank(message = "이름은 필수입니다.")
    private String userName;

    private boolean isOver14;
    private boolean agreeTerms;
    private boolean agreePrivacy;
    private boolean agreeMarketingSns;
    private boolean agreeMarketingEmail;

    @NotBlank(message = "소셜 ID는 필수입니다.")
    private String socialId;
}
