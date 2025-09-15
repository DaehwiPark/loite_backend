package com.boot.loiteBackend.admin.mainpage.popup.controller;

import com.boot.loiteBackend.admin.mainpage.popup.dto.*;
import com.boot.loiteBackend.admin.mainpage.popup.service.AdminMainpagePopupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/mainpage/popup")
@Tag(name = "팝업 등록하기", description = "팝업 관리 관련 API")
public class AdminMainpagePopupController {

    private final AdminMainpagePopupService adminMainpagePopupService;

    //생성
    @Operation(
            summary = "팝업 저장(이미지 1장 포함)",
            description = "multipart/form-data 로 JSON(popup)과 이미지(image)를 함께 전송합니다."
    )
    @PostMapping(path = "/test", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdminMainPagePopupIdDto> create(
            @RequestPart("popup") @Valid AdminMainpagePopupCreateDto req,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) throws IOException {
        Long id = adminMainpagePopupService.create(req, image);
        return ResponseEntity.status(HttpStatus.CREATED).body(new AdminMainPagePopupIdDto(id));
    }

    @Operation(summary = "수정", description = "팝업 내용을 수정")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id,
                                       @Valid @RequestBody AdminMainpagePopupUpdateDto req) {
        adminMainpagePopupService.update(id, req);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "리스트 조회", description = "팝업 리스트 조회")
    @GetMapping
    public ResponseEntity<List<AdminMainpagePopupListItemDto>> listAllForAdmin(
            @RequestParam(required = false) Integer titleMax,
            @RequestParam(required = false) Integer detailMax
    ) {
        return ResponseEntity.ok(adminMainpagePopupService.listAllForAdminSummary(titleMax, detailMax));
    }

    @Operation(summary = "실제 노출", description = "관리자가 아닌 사용자에게 직접 보여줄 팝업리스트 반환")
    @GetMapping("/visible")
    public ResponseEntity<List<AdminMainpagePopupDetailDto>> visible() {
        return ResponseEntity.ok(adminMainpagePopupService.listVisible(LocalDateTime.now()));
    }

    @Operation(summary = "노출 여부", description = "단건 노출 여부 변경")
    @PostMapping("/{id}/active")
    public ResponseEntity<Void> setActive(@PathVariable Long id, @RequestParam boolean active) {
        adminMainpagePopupService.setActive(id, active);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "일괄 노출 여부 변경", description = "모든 팝업 노출 여부 변경")
    @PostMapping("/bulk-active")
    public ResponseEntity<Void> bulkActive(@Valid @RequestBody AdminMainpagePopupBulkActiveDto req) {
        adminMainpagePopupService.bulkSetActive(req.getIds(), req.isActive());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "정렬 저장", description = "팝업 노출 우선 순위 변경")
    @PostMapping("/reorder")
    public ResponseEntity<Void> reorder(@Valid @RequestBody AdminMainpagePopupReorderDto req) {
        adminMainpagePopupService.reorder(req.getOrderedIds());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "소프트 삭제", description = "팝업 소프트 삭제(필요없으면 삭제)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        adminMainpagePopupService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "단건 조회", description = "팝업 단건 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<AdminMainpagePopupDetailDto> get(@PathVariable Long id) {
        return ResponseEntity.ok(adminMainpagePopupService.getOne(id));
    }
}
