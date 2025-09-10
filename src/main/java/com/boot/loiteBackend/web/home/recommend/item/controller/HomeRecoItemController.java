package com.boot.loiteBackend.web.home.recommend.item.controller;

import com.boot.loiteBackend.web.home.recommend.item.dto.HomeRecoItemResponseDto;
import com.boot.loiteBackend.web.home.recommend.item.service.HomeRecoItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/reco/item")
@Tag(name = "메인 추천상품 API", description = "홈 추천 섹션 리스트 조회")
public class HomeRecoItemController {

    private final HomeRecoItemService homeRecoItemService;

    @Operation(
            summary = "섹션 아이템 조회",
            description = "sectionId에 해당하는 추천 아이템을 조회하고, 각 아이템의 product_id로 제품 및 카테고리 정보를 함께 반환합니다."
    )
    @GetMapping
    public ResponseEntity<List<HomeRecoItemResponseDto>> listBySection(
            @Parameter(description = "섹션 ID", required = true)
            @RequestParam Long sectionId
    ) {
        var result = homeRecoItemService.findItemsBySection(sectionId);
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }
        return ResponseEntity.ok(result);
    }
}
