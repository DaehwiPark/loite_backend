package com.boot.loiteBackend.web.home.best.controller;

import com.boot.loiteBackend.web.home.best.dto.HomeBestItemResponseDto;
import com.boot.loiteBackend.web.home.best.service.HomeBestItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/best/item")
@Tag(name = "메인 인기상품 API", description = "홈 인기상품 리스트 조회")
public class HomeBestItemController {

    private final HomeBestItemService homeBestItemService;

    @Operation(
            summary = "인기상품 조회",
            description = "상품/카테고리/대표이미지를 포함한 인기상품 리스트를 반환합니다."
    )
    @GetMapping
    public ResponseEntity<List<HomeBestItemResponseDto>> list() {
        return ResponseEntity.ok(homeBestItemService.getBestItemList());
    }
}
