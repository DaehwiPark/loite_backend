package com.boot.loiteBackend.web.product.controller;

import com.boot.loiteBackend.web.product.dto.ProductDetailResponseDto;
import com.boot.loiteBackend.web.product.dto.ProductListResponseDto;
import com.boot.loiteBackend.web.product.dto.ProductMainResponseDto;
import com.boot.loiteBackend.web.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/public/product")
@Tag(name = "쇼핑몰 상품 조회", description = "쇼핑몰 상품 조회 API")
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "메인페이지 상품 조회", description = "메인페이지 상품을 조회합니다.")
    @GetMapping("/main")
    public ResponseEntity<List<ProductMainResponseDto>> getMainProducts() {
        List<ProductMainResponseDto> products = productService.getMainProducts();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "상품리스트 페이지 목록 조회", description = "상품리스트 페이지 목록을 조회합니다.")
    @GetMapping("/list")
    public ResponseEntity<Page<ProductListResponseDto>> getListProducts(
            @RequestParam String categoryPath,
            @Parameter(description = "정렬 기준 (베스트순, 신상품순, 높은조회순, 리뷰많은순, 낮은가격순, 높은가격순)")
            @RequestParam(defaultValue = "베스트순") String sortType,
            @ParameterObject @PageableDefault(size = 12) Pageable pageable) {
        return ResponseEntity.ok(productService.getListProducts(categoryPath, sortType, pageable));
    }

    @Operation(summary = "상품상세 페이지 상품정보 조회", description = "상품상세 페이지 상품정보를 조회합니다.")
    @GetMapping("/detail/{productId}")
    public ResponseEntity<ProductDetailResponseDto> getDetailProducts(@PathVariable Long productId) {
        ProductDetailResponseDto dto = productService.getDetailProducts(productId);
        return ResponseEntity.ok(dto);
    }
}
