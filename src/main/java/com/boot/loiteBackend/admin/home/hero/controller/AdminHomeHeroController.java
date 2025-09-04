package com.boot.loiteBackend.admin.home.hero.controller;

import com.boot.loiteBackend.admin.home.hero.dto.*;
import com.boot.loiteBackend.admin.home.hero.service.AdminHomeHeroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/home/hero")
@Tag(name = "메인페이지 첫번째 섹션 API", description = "Home Hero(히어로 배너) 관리")
@Validated
public class AdminHomeHeroController {

    private final AdminHomeHeroService adminHomeHeroService;
    private final ObjectMapper objectMapper;

    @Operation(summary = "등록", description = "히어로 섹션 신규 등록 (DTO JSON 문자열 + 이미지 파일 업로드)")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdminHomeHeroResponseDto> create(
            @Parameter(description = "히어로 섹션 생성 DTO", required = true)
            @RequestPart("info") AdminHomeHeroCreateRequestDto info,

            @Parameter(description = "히어로 섹션 이미지 파일", required = true)
            @RequestPart("image") MultipartFile image
    ) {
        return ResponseEntity.status(201)
                .body(adminHomeHeroService.create(info, image));
    }

    @Operation(summary = "수정", description = "히어로 섹션 수정 (DTO + 이미지 파일 업로드 가능)")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminHomeHeroResponseDto> update(
            @Parameter(description = "HOME_HERO_ID") @PathVariable Long id,

            @Parameter(description = "히어로 섹션 수정 DTO", required = true)
            @Valid @RequestPart("updateRequestDto") AdminHomeHeroUpdateRequestDto updateRequestDto,

            @Parameter(description = "히어로 섹션 이미지 파일(선택)")
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        updateRequestDto.setId(id);
        return ResponseEntity.ok(adminHomeHeroService.update(updateRequestDto, image));
    }

    @Operation(summary = "삭제(소프트)", description = "DELETED_YN='Y'로 마킹합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "HOME_HERO_ID") @PathVariable Long id,
            @Valid @RequestBody AdminHomeHeroDeleteRequestDto req
    ) {
        if (!id.equals(req.getHomeHeroId())) {
            throw new IllegalArgumentException("Path id와 요청 본문의 id가 다릅니다.");
        }
        adminHomeHeroService.delete(req);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "상세보기", description = "ID로 단건 조회")
    @GetMapping("/{id}")
    public ResponseEntity<AdminHomeHeroResponseDto> detail(
            @Parameter(description = "HOME_HERO_ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok(adminHomeHeroService.detail(id));
    }

    @Operation(summary = "목록(페이지)", description = "페이지당 10건 기본. sort_order ASC, start_at DESC, id DESC")
    @GetMapping
    public ResponseEntity<Page<AdminHomeHeroResponseDto>> list(
            @ParameterObject AdminHomeHeroListRequestDto filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(adminHomeHeroService.list(filter, page, size));
    }
}
