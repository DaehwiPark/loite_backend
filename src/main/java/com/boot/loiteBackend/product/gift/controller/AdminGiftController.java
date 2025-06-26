package com.boot.loiteBackend.product.gift.controller;

import com.boot.loiteBackend.product.gift.dto.AdminGiftRequestDto;
import com.boot.loiteBackend.product.gift.dto.AdminGiftResponseDto;
import com.boot.loiteBackend.product.gift.service.AdminGiftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/product/gift")
@RequiredArgsConstructor
@Tag(name = "Gift Management", description = "사은품 관리 API")
public class AdminGiftController {

    private final AdminGiftService adminGiftService;

    @Operation(summary = "사은품 등록", description = "사은품을 등록합니다.")
    @PostMapping
    public ResponseEntity<Long> saveGift(@RequestBody AdminGiftRequestDto dto) {
        Long giftId = adminGiftService.saveGift(dto);
        return ResponseEntity.ok(giftId);
    }

    @Operation(summary = "사은품 수정", description = "사은품을 수정합니다.")
    @PutMapping("/{giftId}")
    public ResponseEntity<String> updateGift(@PathVariable Long giftId, @RequestBody AdminGiftRequestDto dto) {
        adminGiftService.updateGift(giftId, dto);
        return ResponseEntity.ok("사은품이 수정되었습니다.");
    }

    @Operation(summary = "사은품 조회", description = "사은품을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<AdminGiftResponseDto>> getAllGifts() {
        return ResponseEntity.ok(adminGiftService.getAllGifts());
    }

    @Operation(summary = "사은품 삭제", description = "사은품을 삭제합니다.")
    @DeleteMapping("/{giftId}")
    public ResponseEntity<String> deleteGift(@PathVariable Long giftId) {
        adminGiftService.deleteGift(giftId);
        return ResponseEntity.ok("사은품이 삭제되었습니다.");
    }
}
