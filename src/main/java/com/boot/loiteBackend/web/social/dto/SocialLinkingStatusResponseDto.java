package com.boot.loiteBackend.web.social.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SocialLinkingStatusResponseDto {
    private String userRegisterType;
    private List<SocialLinkingDto> socialLinking;
}