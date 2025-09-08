package com.boot.loiteBackend.admin.home.fullbanner.controller;

import com.boot.loiteBackend.admin.home.fullbanner.dto.*;
import com.boot.loiteBackend.admin.home.fullbanner.service.AdminHomeFullBannerService;
import com.boot.loiteBackend.config.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/home/full-banner")
@Tag(name = "풀 배너 관리 API", description = "홈 하단 풀 배너 관리")
@Validated
public class AdminHomeFullBannerController {

    private final AdminHomeFullBannerService service;

    @Operation(summary = "등록", description = "풀 배너 등록 (DTO + 배경 이미지 업로드)")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdminHomeFullBannerResponseDto> create(
            @RequestPart("request") @Valid AdminHomeFullBannerCreateRequestDto request,
            @RequestPart(value = "bgImage", required = false) MultipartFile bgImage,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        Long userId = loginUser != null ? loginUser.getUserId() : null;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request, bgImage, userId));
    }

    @Operation(summary = "수정", description = "풀 배너 수정 (DTO + 배경 이미지 교체 가능)")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdminHomeFullBannerResponseDto> update(
            @PathVariable Long id,
            @RequestPart("request") @Valid AdminHomeFullBannerUpdateRequestDto request,
            @RequestPart(value = "bgImage", required = false) MultipartFile bgImage,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        request.setId(id);
        Long userId = loginUser != null ? loginUser.getUserId() : null;
        return ResponseEntity.ok(service.update(request, bgImage, userId));
    }

    @Operation(summary = "삭제", description = "풀 배너 삭제(물리 삭제, 파일도 커밋 후 삭제)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        Long userId = loginUser != null ? loginUser.getUserId() : null;
        service.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상세", description = "풀 배너 상세 조회")
    @GetMapping("/{id}")
    public ResponseEntity<AdminHomeFullBannerResponseDto> detail(@PathVariable Long id) {
        return ResponseEntity.ok(service.detail(id));
    }

    @Operation(summary = "목록(페이지)", description = "페이지당 10건 기본. sort_order ASC, start_at DESC, id DESC")
    @GetMapping
    public ResponseEntity<Page<AdminHomeFullBannerResponseDto>> list(
            @ParameterObject
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @ParameterObject
            @ModelAttribute AdminHomeFullBannerListRequestDto filter
    ) {
        return ResponseEntity.ok(service.list(pageable, filter));
    }
}
