package com.boot.loiteMsBack.support.resource.controller;

import com.boot.loiteMsBack.product.product.dto.ProductSummaryDto;
import com.boot.loiteMsBack.support.resource.dto.SupportResourceDto;
import com.boot.loiteMsBack.support.resource.dto.SupportResourceRequestDto;
import com.boot.loiteMsBack.support.resource.service.SupportResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/support/resource")
@Tag(name = "고객센터 자료실 API", description = "고객센터 자료실 관련 API")
public class SupportResourceController {

    private final SupportResourceService supportResourceService;


    @Operation(summary = "설명서 미등록 제품 목록 조회", description = "아직 제품 설명서가 등록되지 않은 상품 목록을 조회합니다.")
    @GetMapping("/product")
    public ResponseEntity<List<ProductSummaryDto>> getProductListWithoutResource() {
        List<ProductSummaryDto> productList = supportResourceService.getProductsList();
        return ResponseEntity.ok(productList);
    }


    @Operation(summary = "자료 등록", description = "파일과 제품 ID를 함께 등록합니다.")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SupportResourceDto> uploadResource(
            @RequestPart("file") MultipartFile file,
            @RequestPart("info") SupportResourceRequestDto request
    ) {
        SupportResourceDto result = supportResourceService.createResource(request, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Operation(summary = "자료 수정", description = "기존 자료를 수정합니다.")
    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SupportResourceDto> updateResource(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("info") SupportResourceRequestDto request
    ) {
        SupportResourceDto result = supportResourceService.updateResource(id, request, file);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "자료 삭제", description = "자료를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        supportResourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "자료 목록 페이징 조회 (검색 & 페이지네이션)", description = "검색 키워드와 페이지 정보를 기반으로 자료 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<SupportResourceDto>> getPagedResources(
            @PageableDefault(page = 0, size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String keyword
    ) {
        Page<SupportResourceDto> result = supportResourceService.getPagedResources(keyword, pageable);
        return ResponseEntity.ok(result);
    }


    @Operation(summary = "자료 상세 조회", description = "자료 ID로 상세 정보를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<SupportResourceDto> getResourceById(@PathVariable Long id) {
        SupportResourceDto dto = supportResourceService.getResourceById(id);
        return ResponseEntity.ok(dto);
    }

    @Operation(summary = "첨부파일 다운로드", description = "설명서 ID를 통해 첨부파일을 다운로드합니다.")
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        return supportResourceService.fileDownload(id);
    }
}
