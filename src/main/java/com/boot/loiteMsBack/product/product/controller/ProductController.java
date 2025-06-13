package com.boot.loiteMsBack.product.product.controller;

import com.boot.loiteMsBack.product.product.dto.ProductRequestDto;
import com.boot.loiteMsBack.product.product.service.ProductService;
import com.boot.loiteMsBack.util.file.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<Long> registerProduct(
            @RequestPart("product") ProductRequestDto dto,
            @RequestPart("thumbnailImages") List<MultipartFile> thumbnailImages,
            @RequestPart("detailImages") List<MultipartFile> detailImages,
            @RequestPart("mainThumbnailIndex") Integer mainThumbnailIndex
    ) throws IOException {
        Long savedId = productService.saveProduct(dto, thumbnailImages, detailImages, mainThumbnailIndex);
        return ResponseEntity.ok(savedId);
    }
}
