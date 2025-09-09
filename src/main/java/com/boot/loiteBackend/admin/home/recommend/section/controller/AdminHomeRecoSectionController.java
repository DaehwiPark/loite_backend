package com.boot.loiteBackend.admin.home.recommend.section.controller;

import com.boot.loiteBackend.admin.home.recommend.section.dto.AdminHomeRecoSectionCreateDto;
import com.boot.loiteBackend.admin.home.recommend.section.dto.AdminHomeRecoSectionDetailResponseDto;
import com.boot.loiteBackend.admin.home.recommend.section.dto.AdminHomeRecoSectionResponseDto;
import com.boot.loiteBackend.admin.home.recommend.section.dto.AdminHomeRecoSectionUpdateDto;
import com.boot.loiteBackend.admin.home.recommend.section.service.AdminHomeRecoSectionService;
import com.boot.loiteBackend.config.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.*;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/home/reco/sections")
@Tag(name = "홈 추천 섹션 관리 API", description = "좌측 박스(타이틀/링크/노출/정렬) 관리")
public class AdminHomeRecoSectionController {

    private final AdminHomeRecoSectionService adminHomeRecoSectionService;

    @Operation(
            summary = "섹션 등록",
            description = "신규 추천 섹션을 생성합니다. " +
                          "정렬 위치(sortOrder)를 지정하면 그 위치부터 뒤 항목들이 자동으로 뒤로 밀립니다(중복 방지). " +
                          "linkTarget 미지정 시 '_self', displayYn 미지정 시 'Y', sortOrder 미지정 시 마지막에 배치됩니다."
    )
    @PostMapping
    public ResponseEntity<AdminHomeRecoSectionResponseDto> create(
            @RequestBody AdminHomeRecoSectionCreateDto CreateDto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        Long userId = user != null ? user.getUserId() : null;
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(adminHomeRecoSectionService.create(CreateDto, userId));
    }

    @Operation(
            summary = "섹션 수정",
            description = "기존 추천 섹션을 수정합니다. " +
                          "정렬 위치(sortOrder)를 변경하면 기존 순서에서 새 위치까지 구간의 항목들이 자동으로 재정렬 됩니다(중복 방지)."
    )
    @PutMapping("/{id}")
    public ResponseEntity<AdminHomeRecoSectionResponseDto> update(
            @PathVariable Long id,
            @RequestBody AdminHomeRecoSectionUpdateDto UpdateDto,
            @AuthenticationPrincipal CustomUserDetails user
    ) {
        UpdateDto.setId(id);
        Long userId = user != null ? user.getUserId() : null;
        return ResponseEntity.ok(adminHomeRecoSectionService.update(UpdateDto, userId));
    }

    @Operation(
            summary = "섹션 삭제",
            description = "지정한 섹션을 삭제합니다. 삭제 후에는 정렬값을 자동으로 당겨오지 않고 그대로 유지합니다."
    )

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails user) {
        Long userId = user != null ? user.getUserId() : null;
        adminHomeRecoSectionService.delete(id, userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "섹션 상세", description = "섹션의 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<AdminHomeRecoSectionDetailResponseDto> detail(@PathVariable Long id) {
        return ResponseEntity.ok(adminHomeRecoSectionService.detail(id));
    }

    @Operation(
            summary = "섹션 목록(페이지)",
            description = "섹션 목록을 페이지로 조회합니다. " +
                          "기본 정렬은 sortOrder ASC, id DESC 입니다. keyword가 있으면 타이틀에 부분 일치 검색합니다."
    )
    @GetMapping
    public ResponseEntity<Page<AdminHomeRecoSectionResponseDto>> list(
            @ParameterObject
            @PageableDefault(page = 0, size = 10) Pageable pageable,
            @RequestParam(required = false) String keyword
    ) {
        return ResponseEntity.ok(adminHomeRecoSectionService.list(pageable, keyword));
    }
}

