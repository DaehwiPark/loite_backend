// package: com.boot.loiteBackend.admin.mainpage.popup.dto
package com.boot.loiteBackend.admin.mainpage.popup.dto;

import com.boot.loiteBackend.domain.mainpage.popup.MainpagePopupEntity.Target;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AdminMainpagePopupListItemDto {
    private Long popupId;

    private String popupTitle;
    private String popupDetail;

    private String popupImageUrl;
    private String popupLinkUrl;
    private Target popupTarget;
    private boolean popupIsActive;
    private int popupSortOrder;
    private LocalDateTime popupStartAt;
    private LocalDateTime popupEndAt;
    private LocalDateTime createdAt;
}