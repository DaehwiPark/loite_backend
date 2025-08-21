package com.boot.loiteBackend.admin.product.product.controller;

import com.boot.loiteBackend.admin.product.product.dto.AdminProductDetailResponseDto;
import com.boot.loiteBackend.admin.product.product.dto.AdminProductListResponseDto;
import com.boot.loiteBackend.admin.product.product.dto.AdminProductRequestDto;
import com.boot.loiteBackend.admin.product.product.service.AdminProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/product/product")
@Tag(name = "상품 API", description = "상품 관리 API")
public class AdminProductController {

    private final AdminProductService adminProductService;

    @Operation(summary = "상품 등록", description = "상품을 등록합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerProduct(
            @RequestPart("product") AdminProductRequestDto dto,
            @RequestPart(value = "thumbnailImages", required = false) List<MultipartFile> thumbnailImages
    ) throws IOException {
        Long savedId = adminProductService.saveProduct(dto, thumbnailImages);
        return ResponseEntity.ok("ID가 " + savedId + "인 상품이 등록되었습니다.");
    }

    @Operation(summary = "상품 수정", description = "상품을 수정합니다.")
    @PutMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> updateProduct(
            @PathVariable Long productId,
            @RequestPart("product") AdminProductRequestDto dto,
            @RequestPart(value = "thumbnailImages", required = false) List<MultipartFile> thumbnailImages
    ) throws IOException {
        dto.setProductId(productId);
        adminProductService.updateProduct(dto, thumbnailImages);
        return ResponseEntity.ok("ID가 " + productId + "인 상품이 수정되었습니다.");
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        adminProductService.deleteProduct(productId);
        return ResponseEntity.ok("상품이 삭제되었습니다.");
    }

    @Operation(summary = "상품 목록 조회", description = "상품 목록을 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<Page<AdminProductListResponseDto>> getPagedProducts(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<AdminProductListResponseDto> result = adminProductService.getPagedProducts(keyword, pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "상품 상세 조회", description = "상품 ID로 상세 정보를 조회합니다.")
    @GetMapping("/{productId}")
    public ResponseEntity<AdminProductDetailResponseDto> getProductDetail(@PathVariable Long productId) {
        AdminProductDetailResponseDto productDetail = adminProductService.getAllProductDetail(productId);
        return ResponseEntity.ok(productDetail);
    }
}

