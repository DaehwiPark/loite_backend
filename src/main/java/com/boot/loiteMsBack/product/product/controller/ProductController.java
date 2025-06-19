package com.boot.loiteMsBack.product.product.controller;

import com.boot.loiteMsBack.product.product.dto.ProductDetailResponseDto;
import com.boot.loiteMsBack.product.product.dto.ProductListResponseDto;
import com.boot.loiteMsBack.product.product.dto.ProductRequestDto;
import com.boot.loiteMsBack.product.product.service.ProductService;
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
@RequestMapping("/api/product/product")
@Tag(name = "Product Management", description = "상품 관리 API")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "상품 등록", description = "상품을 등록합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> registerProduct(
            @RequestPart("product") ProductRequestDto dto,
            @RequestPart("thumbnailImages") List<MultipartFile> thumbnailImages,
            @RequestPart("detailImages") List<MultipartFile> detailImages,
            @RequestPart("mainThumbnailIndex") Integer mainThumbnailIndex
    ) throws IOException {
        Long savedId = productService.saveProduct(dto, thumbnailImages, detailImages, mainThumbnailIndex);
        return ResponseEntity.ok("ID가 " +savedId + "인 상품이 등록되었습니다.");
    }

    @Operation(summary = "상품 수정", description = "상품을 수정합니다.")
    @PutMapping("/{productId}")
    public ResponseEntity<String> updateProduct(
            @PathVariable Long productId,
            @RequestPart("product") ProductRequestDto dto,
            @RequestPart(value = "thumbnailImages", required = false) List<MultipartFile> thumbnailImages,
            @RequestPart(value = "detailImages", required = false) List<MultipartFile> detailImages,
            @RequestParam(value = "mainThumbnailIndex", required = false) Integer mainThumbnailIndex
    ) throws IOException {
        dto.setProductId(productId);
        productService.updateProduct(dto, thumbnailImages, detailImages, mainThumbnailIndex);
        return ResponseEntity.ok("ID가 " +productId + "인 상품이 수정되었습니다.");
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok("상품이 삭제되었습니다.");
    }

    @GetMapping("/list")
    public ResponseEntity<Page<ProductListResponseDto>> getPagedProducts(
            @RequestParam(required = false) String keyword,
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Page<ProductListResponseDto> result = productService.getPagedProducts(keyword, pageable);
        return ResponseEntity.ok(result);
    }

    // 상품 상세 조회
    @Operation(summary = "상품 상세 조회", description = "상품 ID로 상세 정보를 조회합니다.")
    @GetMapping("/{productId}")
    public ResponseEntity<ProductDetailResponseDto> getProductDetail(@PathVariable Long productId) {
        ProductDetailResponseDto productDetail = productService.getAllProductDetail(productId);
        return ResponseEntity.ok(productDetail);
    }
}
