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
public class AdminMainpagePopupCreateDto {
    @Size(max = 100)
    private String popupTitle;

    @Size(max = 100)
    private String popupDetail;

    @NotBlank
    @Size(max = 512)
    private String popupLinkUrl;

    @Builder.Default
    private MainpagePopupEntity.Target popupTarget = MainpagePopupEntity.Target._self;

    @Builder.Default
    private boolean popupIsActive = true;

    private LocalDateTime popupStartAt;
    private LocalDateTime popupEndAt;

    // 이미지 없음: popupImageUrl 은 아예 보내지 않음(서버에서 null 저장)
}
