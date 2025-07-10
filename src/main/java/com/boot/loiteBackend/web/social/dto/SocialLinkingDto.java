package com.boot.loiteBackend.web.social.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SocialLinkingDto {
    private String socialType;
    private String email;
    private String name;
    private LocalDateTime connectedAt;
    private boolean isRegisterAccount;
}
