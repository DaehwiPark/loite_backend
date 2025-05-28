package com.boot.loiteMsBack.support.resource.controller;

import com.boot.loiteMsBack.support.resource.dto.SupportResourceDto;
import com.boot.loiteMsBack.support.resource.dto.SupportResourceRequestDto;
import com.boot.loiteMsBack.support.resource.service.SupportResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/support/resource")
@Tag(name = "Support Resource", description = "고객센터 자료실 관련 API")
public class SupportResourceController {

    private final SupportResourceService supportResourceService;

    public SupportResourceController(SupportResourceService supportResourceService) {
        this.supportResourceService = supportResourceService;
    }

    /**
     * 자료 등록
     */
    @Operation(summary = "자료 등록", description = "파일과 정보를 함께 등록합니다.")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SupportResourceDto uploadResource(
            @RequestPart("file") MultipartFile file,
            @RequestPart("info") SupportResourceRequestDto request
    ) {
        return supportResourceService.createResource(request, file);
    }


    /**
     * 자료 수정
     */
    @Operation(summary = "자료 수정", description = "기존 자료를 수정합니다.")
    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public SupportResourceDto updateResource(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("info") SupportResourceRequestDto request
    ) {
        return supportResourceService.updateResource(id, request, file);
    }

    /**
     * 자료 삭제
     */
    @Operation(summary = "자료 삭제", description = "자료를 삭제합니다.")
    @DeleteMapping("/{id}")
    public void deleteResource(@PathVariable Long id) {
        supportResourceService.deleteResource(id);
    }

    /**
     * 자료 전체 조회
     */
    @Operation(summary = "자료 전체 조회", description = "등록된 모든 자료를 조회합니다.")
    @GetMapping
    public List<SupportResourceDto> getAllResources() {
        return supportResourceService.getAllResources();
    }

    /**
     * 자료 단건 조회
     */
    @Operation(summary = "자료 단건 조회", description = "ID를 기준으로 자료를 조회합니다.")
    @GetMapping("/{id}")
    public SupportResourceDto getResourceById(@PathVariable Long id) {
        return supportResourceService.getResourceById(id);
    }
}
