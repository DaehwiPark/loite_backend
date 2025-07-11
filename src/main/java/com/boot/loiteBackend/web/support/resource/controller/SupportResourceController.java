package com.boot.loiteBackend.web.support.resource.controller;

import com.boot.loiteBackend.web.support.resource.dto.SupportResourceDto;
import com.boot.loiteBackend.web.support.resource.service.SupportResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/support/resource")
@Tag(name = "쇼핑몰 고객센터 자료실 API", description = "제품 설명서 다운로드 및 검색 API")
public class SupportResourceController {

    private final SupportResourceService supportResourceService;

    @Operation(summary = "제품 매뉴얼 리스트 조회", description = "모델명 또는 제품명으로 검색하고 페이징 처리된 매뉴얼 리스트를 반환합니다.")
    @GetMapping
    public ResponseEntity<Page<SupportResourceDto>> getManualList(
            @Parameter(description = "검색 키워드") @RequestParam(required = false) String keyword,
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.ASC) Pageable pageable
    ) {
        return ResponseEntity.ok(supportResourceService.getManuals(keyword, pageable));
    }

    @Operation(summary = "매뉴얼 파일 다운로드", description = "지정된 매뉴얼 ID를 기반으로 첨부파일을 다운로드합니다.")
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadManual(@PathVariable Long id) {
        return supportResourceService.fileDownload(id);
    }
}