package com.boot.loiteMsBack.product.brand.controller;

import com.boot.loiteMsBack.product.brand.dto.ProductBrandRequestDto;
import com.boot.loiteMsBack.product.brand.dto.ProductBrandResponseDto;
import com.boot.loiteMsBack.product.brand.service.ProductBrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/product/brand")
@Tag(name = "Brand Management", description = "브랜드 관리 API")
public class ProductBrandController {
    private final ProductBrandService productBrandService;

    @Operation(summary = "브랜드 등록", description = "브랜드를 등록합니다.")
    @PostMapping
    public ResponseEntity<Long> registerBrand(@RequestBody ProductBrandRequestDto dto){
        Long savedId = productBrandService.saveBrand(dto);

        return ResponseEntity.ok(savedId);
    }

    @GetMapping
    public List<ProductBrandResponseDto> getBrands(){
        return productBrandService.getAllBrands();
    }
}
