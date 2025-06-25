package com.boot.loiteBackend.product.brand.controller;

import com.boot.loiteBackend.product.brand.dto.ProductBrandRequestDto;
import com.boot.loiteBackend.product.brand.dto.ProductBrandResponseDto;
import com.boot.loiteBackend.product.brand.service.ProductBrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    @PutMapping("/{brandId}")
    public ResponseEntity<Void> updateBrand(@PathVariable Long brandId, @RequestBody @Valid ProductBrandRequestDto dto) {
        productBrandService.updateBrand(brandId, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{brandId}")
    public ResponseEntity<String> deleteBrand(@PathVariable Long brandId){
        productBrandService.deleteBrand(brandId);
        return ResponseEntity.ok("브랜드가 삭제되었습니다.");
    }
}
