package com.boot.loiteBackend.admin.mainpage.popup.dto;

import com.boot.loiteBackend.domain.mainpage.popup.MainpagePopupEntity;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminMainpagePopupUpdateDto {
    @Size(max = 512)
    private String popupImageUrl;

    @Size(max = 512)
    private String popupLinkUrl;

    private MainpagePopupEntity.Target popupTarget;

    private Boolean popupIsActive;
    private Integer popupSortOrder;

    private LocalDateTime popupStartAt;
    private LocalDateTime popupEndAt;
}
