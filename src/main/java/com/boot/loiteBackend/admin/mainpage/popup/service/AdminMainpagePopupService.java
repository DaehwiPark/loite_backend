package com.boot.loiteBackend.admin.mainpage.popup.service;

import com.boot.loiteBackend.admin.mainpage.popup.dto.AdminMainpagePopupDetailDto;
import com.boot.loiteBackend.admin.mainpage.popup.dto.AdminMainpagePopupDto;
import com.boot.loiteBackend.admin.mainpage.popup.dto.AdminMainpagePopupUpdateDto;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminMainpagePopupService {
    // 어드민 리스트(활성/비활성 포함, 정렬순)
    List<AdminMainpagePopupDetailDto> listAllForAdmin();

    // 실제 노출 대상(활성 + 기간조건 충족)
    List<AdminMainpagePopupDetailDto> listVisible(LocalDateTime now);

    // 신규 등록 (정렬값은 MAX+10 자동 배정)
    Long create(AdminMainpagePopupDto req);

    // 수정 (링크/타겟/기간/활성/이미지 경로 등)
    void update(Long popupId, AdminMainpagePopupUpdateDto req);

    // 활성/비활성 토글(단건)
    void setActive(Long popupId, boolean active);

    // 활성/비활성 일괄 변경
    void bulkSetActive(List<Long> ids, boolean active);

    // 드래그앤드롭 후 순서 저장 (id 배열의 순서대로 10,20,30...)
    void reorder(List<Long> orderedIds);

    // 소프트 삭제(비활성+삭제시각 기록)
    void softDelete(Long popupId);

}
