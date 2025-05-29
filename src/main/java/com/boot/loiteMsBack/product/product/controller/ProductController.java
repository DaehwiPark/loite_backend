package com.boot.loiteMsBack.product.product.controller;

import com.boot.loiteMsBack.product.product.dto.ProductRequestDto;
import com.boot.loiteMsBack.product.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product/product")
@Tag(name = "Product Management", description = "상품 관리 API")
public class ProductController {
    private final ProductService productService;

    @Operation(summary = "상품 등록", description = "상품을 등록합니다.")
    @PostMapping
    public ResponseEntity<Long> registerProduct(@RequestBody ProductRequestDto dto){
        Long savedId = productService.saveProduct(dto);

        return ResponseEntity.ok(savedId);
    }
}
