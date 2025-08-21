package com.boot.loiteBackend.admin.product.brand.controller;

import com.boot.loiteBackend.admin.product.brand.dto.AdminProductBrandRequestDto;
import com.boot.loiteBackend.admin.product.brand.dto.AdminProductBrandResponseDto;
import com.boot.loiteBackend.admin.product.brand.service.AdminProductBrandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/product/brand")
@Tag(name = "브랜드 API", description = "브랜드 관리 API")
public class AdminProductBrandController {
    private final AdminProductBrandService adminProductBrandService;

    @Operation(summary = "브랜드 등록", description = "브랜드를 등록합니다.")
    @PostMapping
    public ResponseEntity<Long> registerBrand(@RequestBody AdminProductBrandRequestDto dto){
        Long savedId = adminProductBrandService.saveBrand(dto);

        return ResponseEntity.ok(savedId);
    }

    @Operation(summary = "브랜드 조회", description = "브랜드를 조회합니다.")
    @GetMapping
    public List<AdminProductBrandResponseDto> getBrands(){
        return adminProductBrandService.getAllBrands();
    }

    @Operation(summary = "브랜드 수정", description = "브랜드를 수정합니다.")
    @PutMapping("/{brandId}")
    public ResponseEntity<Void> updateBrand(@PathVariable Long brandId, @RequestBody @Valid AdminProductBrandRequestDto dto) {
        adminProductBrandService.updateBrand(brandId, dto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "브랜드 삭제", description = "브랜드를 삭제합니다.")
    @DeleteMapping("/{brandId}")
    public ResponseEntity<String> deleteBrand(@PathVariable Long brandId){
        adminProductBrandService.deleteBrand(brandId);
        return ResponseEntity.ok("브랜드가 삭제되었습니다.");
    }
}
