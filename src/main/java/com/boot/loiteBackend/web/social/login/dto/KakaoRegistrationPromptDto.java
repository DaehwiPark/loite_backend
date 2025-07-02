package com.boot.loiteBackend.web.social.login.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoRegistrationPromptDto {
    private String code;
    private String email;
    private String socialId;
}
