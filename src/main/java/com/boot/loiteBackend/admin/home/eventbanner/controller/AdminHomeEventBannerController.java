package com.boot.loiteBackend.admin.home.eventbanner.controller;

import com.boot.loiteBackend.admin.home.eventbanner.dto.*;
import com.boot.loiteBackend.admin.home.eventbanner.service.AdminHomeEventBannerService;
import com.boot.loiteBackend.config.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/home/banner")
@Tag(name = "이벤트 배너 관리 API", description = "배너 관리")
public class AdminHomeEventBannerController {

    private final AdminHomeEventBannerService adminHomeEventBannerService;

    @Operation(summary = "등록", description = "배너 등록 (DTO + PC/Mobile 이미지 업로드)")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdminHomeEventBannerResponseDto> create(
            @Parameter(description = "배너 생성 DTO", required = true)
            @RequestPart("request") @Valid AdminHomeEventBannerCreateRequestDto request,
            @Parameter(description = "PC 이미지", required = false)
            @RequestPart(value = "pcImage", required = false) MultipartFile pcImage,
            @Parameter(description = "모바일 이미지", required = false)
            @RequestPart(value = "mobileImage", required = false) MultipartFile mobileImage,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        Long userId = (loginUser != null) ? loginUser.getUserId() : null;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminHomeEventBannerService.create(request, pcImage, mobileImage, userId));
    }

    @Operation(summary = "수정", description = "배너 수정 (DTO + PC/Mobile 이미지 교체 가능)")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdminHomeEventBannerResponseDto> update(
            @PathVariable Long id,
            @Parameter(description = "배너 수정 DTO", required = true)
            @RequestPart("request") @Valid AdminHomeEventBannerUpdateRequestDto request,
            @Parameter(description = "PC 이미지(선택)")
            @RequestPart(value = "pcImage", required = false) MultipartFile pcImage,
            @Parameter(description = "모바일 이미지(선택)")
            @RequestPart(value = "mobileImage", required = false) MultipartFile mobileImage,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        request.setId(id);
        Long userId = (loginUser != null) ? loginUser.getUserId() : null;
        return ResponseEntity.ok(adminHomeEventBannerService.update(request, pcImage, mobileImage, userId));
    }

    @Operation(summary = "삭제", description = "배너 삭제(물리 삭제)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @Parameter(hidden = true) @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        Long userId = (loginUser != null) ? loginUser.getUserId() : null;
        adminHomeEventBannerService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상세", description = "배너 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<AdminHomeEventBannerResponseDto> detail(@PathVariable Long id) {
        return ResponseEntity.ok(adminHomeEventBannerService.detail(id));
    }

    @Operation(  summary = "목록(페이지)",description = "페이지당 10건 기본. sort_order ASC, start_at DESC, id DESC")
    @GetMapping
    public ResponseEntity<Page<AdminHomeEventBannerResponseDto>> list(
            @ParameterObject
            @PageableDefault(page = 0, size = 10) Pageable pageable, // 정렬은 서비스에서 기본값 적용
            @ParameterObject
            @ModelAttribute AdminHomeEventBannerListRequestDto filter
    ) {
        return ResponseEntity.ok(adminHomeEventBannerService.list(pageable, filter));
    }
}
