package com.boot.loiteBackend.admin.login.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminLoginRequestDto {
    private String email;
    private String password;
}
