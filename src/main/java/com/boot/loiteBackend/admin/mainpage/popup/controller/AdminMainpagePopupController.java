package com.boot.loiteBackend.admin.mainpage.popup.controller;

import com.boot.loiteBackend.admin.mainpage.popup.dto.*;
import com.boot.loiteBackend.admin.mainpage.popup.service.AdminMainpagePopupService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/mainpage/popup")
@Tag(name = "팝업 등록하기", description = "팝업 관리 관련 API")
public class AdminMainpagePopupController {

    private final AdminMainpagePopupService adminMainpagePopupService;

    // 등록
    @PostMapping
    public ResponseEntity<AdminMainPagePopupIdDto> create(@Valid @RequestBody AdminMainpagePopupDto req) {
        Long id = adminMainpagePopupService.create(req);
        return ResponseEntity.ok(new AdminMainPagePopupIdDto(id));
    }

    // 수정
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @Valid @RequestBody AdminMainpagePopupUpdateDto req) {
        adminMainpagePopupService.update(id, req);
        return ResponseEntity.noContent().build();
    }

//    // 관리자 리스트
//    @GetMapping
//    public ResponseEntity<List<AdminMainpagePopupDetailDto>> listAllForAdmin() {
//        return ResponseEntity.ok(adminMainpagePopupService.listAllForAdmin());
//    }

    @GetMapping
    public ResponseEntity<List<AdminMainpagePopupListItemDto>> listAllForAdmin(
            @RequestParam(required = false) Integer titleMax,
            @RequestParam(required = false) Integer detailMax
    ) {
        return ResponseEntity.ok(adminMainpagePopupService.listAllForAdminSummary(titleMax, detailMax));
    }

    // 실제 노출(현재 시각)
    @GetMapping("/visible")
    public ResponseEntity<List<AdminMainpagePopupDetailDto>> visible() {
        return ResponseEntity.ok(adminMainpagePopupService.listVisible(LocalDateTime.now()));
    }

    // 활성 토글(단건)
    @PostMapping("/{id}/active")
    public ResponseEntity<Void> setActive(@PathVariable Long id, @RequestParam boolean active) {
        adminMainpagePopupService.setActive(id, active);
        return ResponseEntity.noContent().build();
    }

    // 일괄 활성/비활성
    @PostMapping("/bulk-active")
    public ResponseEntity<Void> bulkActive(@Valid @RequestBody AdminMainpagePopupBulkActiveDto req) {
        adminMainpagePopupService.bulkSetActive(req.getIds(), req.isActive());
        return ResponseEntity.noContent().build();
    }

    // 정렬 저장
    @PostMapping("/reorder")
    public ResponseEntity<Void> reorder(@Valid @RequestBody AdminMainpagePopupReorderDto req) {
        adminMainpagePopupService.reorder(req.getOrderedIds());
        return ResponseEntity.noContent().build();
    }

    // 소프트 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminMainpagePopupService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    //팝업 저장 테스트
    @PostMapping("/test")
    public ResponseEntity<AdminMainPagePopupIdDto> createTest(@Valid @RequestBody AdminMainpagePopupCreateTestDto req) {
        Long id = adminMainpagePopupService.createTest(req);   // 서비스 메서드 추가 필요
        return ResponseEntity.status(HttpStatus.CREATED).body(new AdminMainPagePopupIdDto(id));
    }

    // /api/admin/mainpage/popup/{id}
    // 한 개만 호출 (자세히 보기용)
    @GetMapping("/{id}")
    public ResponseEntity<AdminMainpagePopupDetailDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(adminMainpagePopupService.getOne(id));
    }

}
