package com.boot.loiteBackend.admin.mainpage.popup.dto;

import com.boot.loiteBackend.domain.mainpage.popup.MainpagePopupEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminMainpagePopupDto {
    @NotBlank
    @Size(max = 512)
    private String popupImageUrl;

    @NotBlank
    @Size(max = 512)
    private String popupLinkUrl;

    @Builder.Default
    private MainpagePopupEntity.Target popupTarget = MainpagePopupEntity.Target._self;

    @Builder.Default
    private boolean popupIsActive = true;

    private LocalDateTime popupStartAt;
    private LocalDateTime popupEndAt;
}
