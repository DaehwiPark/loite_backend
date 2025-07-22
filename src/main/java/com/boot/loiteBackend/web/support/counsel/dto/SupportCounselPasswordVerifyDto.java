package com.boot.loiteBackend.web.support.counsel.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupportCounselPasswordVerifyDto {

    @Schema(description = "비밀글 접근을 위한 입력된 비밀번호", example = "mypassword123!")
    private String inputPassword;
}