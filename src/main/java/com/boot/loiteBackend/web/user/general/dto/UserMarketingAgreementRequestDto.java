package com.boot.loiteBackend.web.user.general.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "마케팅 수신 동의 정보 수정 요청 DTO")
public class UserMarketingAgreementRequestDto {

    @Schema(description = "SNS(문자/카카오톡 등) 마케팅 수신 동의 여부", example = "true", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean agreeMarketingSns;

    @Schema(description = "이메일 마케팅 수신 동의 여부", example = "false", requiredMode = Schema.RequiredMode.REQUIRED)
    private boolean agreeMarketingEmail;
}