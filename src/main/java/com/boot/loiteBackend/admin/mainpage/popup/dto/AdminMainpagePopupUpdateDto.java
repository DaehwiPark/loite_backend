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
//기존 팝업 부분 수정 요청 바디
public class AdminMainpagePopupUpdateDto {
    @Size(max = 100)
    private String popupTitle;

    @Size(max = 100)
    private String popupDetail;

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
