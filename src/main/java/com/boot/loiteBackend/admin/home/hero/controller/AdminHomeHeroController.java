package com.boot.loiteBackend.admin.home.hero.controller;

import com.boot.loiteBackend.admin.home.hero.dto.*;
import com.boot.loiteBackend.admin.home.hero.service.AdminHomeHeroService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/home/hero")
@Tag(name = "히어로 배너 관리 API", description = "Home Hero(히어로 배너) 관리")
@Validated
public class AdminHomeHeroController {

    private final AdminHomeHeroService adminHomeHeroService;

    @Operation(summary = "등록", description = "히어로 섹션 신규 등록 (DTO + 이미지 업로드), 작성자는 로그인 사용자")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdminHomeHeroResponseDto> create(
            @Parameter(description = "히어로 섹션 생성 DTO(JSON part: info)", required = true)
            @Valid @RequestPart(name = "info", required = true) AdminHomeHeroCreateRequestDto info,
            @Parameter(description = "히어로 섹션 이미지 파일", required = true)
            @RequestPart(name = "image", required = true) MultipartFile image,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        Long loginUserId = (loginUser != null) ? loginUser.getUserId() : null;
        return ResponseEntity.status(201)
                .body(adminHomeHeroService.create(info, image, loginUserId));
    }

    @Operation(summary = "수정", description = "히어로 섹션 수정 (DTO + 이미지 파일 선택), 수정자는 로그인 사용자")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminHomeHeroResponseDto> update(
            @Parameter(description = "HOME_HERO_ID", required = true) @PathVariable Long id,
            @Parameter(description = "히어로 섹션 수정 DTO(JSON part: info)", required = true)
            @Valid @RequestPart(name = "info", required = true) AdminHomeHeroUpdateRequestDto info,
            @Parameter(description = "히어로 섹션 이미지 파일(선택)")
            @RequestPart(name = "image", required = false) MultipartFile image,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        info.setId(id);
        Long loginUserId = (loginUser != null) ? loginUser.getUserId() : null;
        return ResponseEntity.ok(adminHomeHeroService.update(info, image, loginUserId));
    }

    @Operation(summary = "삭제(소프트)", description = "DELETED_YN='Y'로 마킹, 수행자는 로그인 사용자")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "HOME_HERO_ID", required = true) @PathVariable Long id,
            @Parameter(hidden = true)
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        Long loginUserId = (loginUser != null) ? loginUser.getUserId() : null;
        adminHomeHeroService.delete(id, loginUserId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "상세보기", description = "ID로 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<AdminHomeHeroResponseDto> detail(
            @Parameter(description = "HOME_HERO_ID", required = true) @PathVariable Long id
    ) {
        return ResponseEntity.ok(adminHomeHeroService.detail(id));
    }

    @Operation(summary = "목록(페이지)", description = "페이지당 10건 기본. sort_order ASC, start_at DESC, id DESC")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<AdminHomeHeroResponseDto>> list(
            @ParameterObject
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @ParameterObject
            @ModelAttribute AdminHomeHeroListRequestDto filter
    ) {
        return ResponseEntity.ok(adminHomeHeroService.list(pageable, filter));
    }
}