package com.boot.loiteBackend.admin.login.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminLogoutResponseDto {
    private boolean success;
    private String message;
}
