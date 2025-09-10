package com.boot.loiteBackend.admin.mainpage.popup.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
// 팝업 생성 성공 시 ID만 반환하는 DTO
public class AdminMainPagePopupIdDto {
    private Long popupId;
}
