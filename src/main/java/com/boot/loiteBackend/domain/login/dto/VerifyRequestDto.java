package com.boot.loiteBackend.domain.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyRequestDto {

    @Schema(description = "비밀번호", example = "1234abcd!")
    private String password;

}
