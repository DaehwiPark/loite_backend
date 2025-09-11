package com.boot.loiteBackend.admin.home.magazinebanner.controller;

import com.boot.loiteBackend.admin.home.magazinebanner.dto.AdminHomeMagazineBannerDto;
import com.boot.loiteBackend.admin.home.magazinebanner.service.AdminHomeMagazineBannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/home/magazine-banner")
@Tag(name = "시즌 상품 배너 관리 API", description = "홈 하단 캐러셀 시즌 상품(웹메거진 이동) 배너 관리")
@Validated
public class AdminHomeMagazineBannerContoller {

    private final AdminHomeMagazineBannerService service;

    @Operation(summary = "배너 등록", description = "홈 매거진 배너를 신규 등록한다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdminHomeMagazineBannerDto.Response> create(
            @Validated @RequestPart("request") AdminHomeMagazineBannerDto.Request request,
            @RequestPart(value = "videoFile", required = false) MultipartFile videoFile,
            @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile
    ) {
        return ResponseEntity.ok(service.create(request, videoFile, thumbnailFile));
    }

    @Operation(summary = "배너 수정", description = "기존 홈 매거진 배너 정보를 수정한다.")
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdminHomeMagazineBannerDto.Response> update(
            @Parameter(description = "배너 ID") @PathVariable Long id,
            @Validated @RequestPart("request") AdminHomeMagazineBannerDto.Request request,
            @RequestPart(value = "videoFile", required = false) MultipartFile videoFile,
            @RequestPart(value = "thumbnailFile", required = false) MultipartFile thumbnailFile
    ) {
        return ResponseEntity.ok(service.update(id, request, videoFile, thumbnailFile));
    }
    @Operation(summary = "배너 삭제", description = "홈 매거진 배너를 삭제한다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "배너 ID") @PathVariable Long id
    ) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "배너 단건 조회", description = "특정 홈 매거진 배너 상세 정보를 조회한다.")
    @GetMapping("/{id}")
    public ResponseEntity<AdminHomeMagazineBannerDto.Response> getOne(
            @Parameter(description = "배너 ID") @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.getOne(id));
    }

    @Operation(summary = "배너 전체 조회 (페이징)", description = "홈 매거진 배너 리스트를 페이징 조회한다.")
    @GetMapping
    public ResponseEntity<Page<AdminHomeMagazineBannerDto.Response>> getList(
            Pageable pageable
    ) {
        return ResponseEntity.ok(service.getList(pageable));
    }
}