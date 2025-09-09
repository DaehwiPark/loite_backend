package com.boot.loiteBackend.admin.mainpage.popup.controller;

import com.boot.loiteBackend.admin.mainpage.popup.dto.AdminMainPagePopupIdDto;
import com.boot.loiteBackend.admin.mainpage.popup.dto.AdminMainpagePopupDto;
import com.boot.loiteBackend.admin.mainpage.popup.service.AdminMainpagePopupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/mainpage/popup")
@Tag(name = "팝업 등록하기", description = "팝업 관리 관련 API")
public class AdminMainpagePopupController {

    private final AdminMainpagePopupService adminMainpagePopupService;

    @PostMapping("/api/admin/mainpage-popups")
    public ResponseEntity<AdminMainPagePopupIdDto> create(@Valid @RequestBody AdminMainpagePopupDto req) {
        Long id = adminMainpagePopupService.create(req);
        return ResponseEntity.ok(new AdminMainPagePopupIdDto(id));
    }
}
