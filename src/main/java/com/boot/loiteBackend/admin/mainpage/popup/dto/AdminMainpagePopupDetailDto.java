package com.boot.loiteBackend.admin.mainpage.popup.dto;

import com.boot.loiteBackend.domain.mainpage.popup.MainpagePopupEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminMainpagePopupDetailDto {
    private Long popupId;

    private String popupImageUrl;
    private String popupLinkUrl;
    private MainpagePopupEntity.Target popupTarget;

    private boolean popupIsActive;
    private int popupSortOrder;

    private LocalDateTime popupStartAt;
    private LocalDateTime popupEndAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime popupDeletedAt;

}


