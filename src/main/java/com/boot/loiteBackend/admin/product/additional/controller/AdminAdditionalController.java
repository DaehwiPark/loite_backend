package com.boot.loiteBackend.admin.product.additional.controller;

import com.boot.loiteBackend.admin.product.additional.dto.AdminAdditionalRequestDto;
import com.boot.loiteBackend.admin.product.additional.dto.AdminAdditionalResponseDto;
import com.boot.loiteBackend.admin.product.additional.service.AdminAdditionalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/product/additional")
@RequiredArgsConstructor
@Tag(name = "추가구성품 API", description = "추가구성품 관리 API")
public class AdminAdditionalController {
    private final AdminAdditionalService adminAdditionalService;

    @Operation(summary = "추가구성품 등록", description = "본상품에 연결된 추가구성품을 등록합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdminAdditionalResponseDto> createAdditional(@ModelAttribute AdminAdditionalRequestDto requestDto, @RequestParam("imageFile") MultipartFile imageFile) {
        AdminAdditionalResponseDto response = adminAdditionalService.createAdditional(requestDto, imageFile);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "추가구성품 수정", description = "추가구성품을 수정합니다.")
    @PutMapping(value = "/{additionalId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdminAdditionalResponseDto> updateAdditional(@PathVariable Long additionalId, @ModelAttribute AdminAdditionalRequestDto requestDto, @RequestParam(value = "imageFile", required = false) MultipartFile imageFile) {
        AdminAdditionalResponseDto response = adminAdditionalService.updateAdditional(additionalId, requestDto, imageFile);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "추가구성품 삭제", description = "추가구성품을 삭제(비활성화)합니다.")
    @DeleteMapping("/{additionalId}")
    public ResponseEntity<String> deleteAdditional(@PathVariable Long additionalId) {
        adminAdditionalService.deleteAdditional(additionalId);
        return ResponseEntity.ok("추가구성품 삭제 완료");
    }

    @Operation(summary = "추가구성품 상세 조회", description = "추가구성품 단건을 조회합니다.")
    @GetMapping("/{additionalId}")
    public ResponseEntity<AdminAdditionalResponseDto> getAdditional(@PathVariable Long additionalId) {
        return ResponseEntity.ok(adminAdditionalService.getAdditional(additionalId));
    }

    @Operation(summary = "추가구성품 목록 조회", description = "추가구성품 전체 목록을 페이징으로 조회합니다.")
    @GetMapping
    public ResponseEntity<Page<AdminAdditionalResponseDto>> getAllAdditionals(@PageableDefault(size = 10, sort = "additionalId", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(adminAdditionalService.getAllAdditionals(pageable));
    }
}
