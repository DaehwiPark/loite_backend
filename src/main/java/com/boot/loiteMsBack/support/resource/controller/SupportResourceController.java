package com.boot.loiteMsBack.support.resource.controller;

import com.boot.loiteMsBack.support.resource.dto.SupportResourceDto;
import com.boot.loiteMsBack.support.resource.dto.SupportResourceRequestDto;
import com.boot.loiteMsBack.support.resource.service.SupportResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/support/resource")
@Tag(name = "Support Resource", description = "고객센터 자료실 관련 API")
public class SupportResourceController {

    private final SupportResourceService supportResourceService;

    @Operation(summary = "자료 등록", description = "파일과 정보를 함께 등록합니다.")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SupportResourceDto> uploadResource(
            @RequestPart("file") MultipartFile file,
            @RequestPart("info") SupportResourceRequestDto request
    ) {
        SupportResourceDto result = supportResourceService.createResource(request, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(result); // 201 Created
    }

    @Operation(summary = "자료 수정", description = "기존 자료를 수정합니다.")
    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SupportResourceDto> updateResource(
            @PathVariable Long id,
            @RequestPart("file") MultipartFile file,
            @RequestPart("info") SupportResourceRequestDto request
    ) {
        SupportResourceDto result = supportResourceService.updateResource(id, request, file);
        return ResponseEntity.ok(result); // 200 OK
    }

    @Operation(summary = "자료 삭제", description = "자료를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        supportResourceService.deleteResource(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @Operation(summary = "자료 전체 조회", description = "등록된 모든 자료를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<SupportResourceDto>> getAllResources() {
        List<SupportResourceDto> list = supportResourceService.getAllResources();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "자료 단건 조회", description = "ID를 기준으로 자료를 조회합니다.")
    @GetMapping("/{id}")
    public ResponseEntity<SupportResourceDto> getResourceById(@PathVariable Long id) {
        SupportResourceDto dto = supportResourceService.getResourceById(id);
        return ResponseEntity.ok(dto);
    }
}
