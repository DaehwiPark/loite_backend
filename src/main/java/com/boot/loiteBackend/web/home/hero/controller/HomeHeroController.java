package com.boot.loiteBackend.web.home.hero.controller;

import com.boot.loiteBackend.web.home.hero.dto.HomeHeroResponseDto;
import com.boot.loiteBackend.web.home.hero.service.HomeHeroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/hero")
@Tag(name = "메인 히어로 API", description = "쇼핑몰 메인 첫 섹션용 읽기 API")
public class HomeHeroController {

    private final HomeHeroService homeHeroService;

    @Operation(
            summary = "활성 히어로 단건",
            description = "DISPLAY_YN='Y', PUBLISH_STATUS='PUBLISHED', 노출기간(start/end) 충족 중인 항목 중 정렬 기준으로 1건"
    )
    @GetMapping
    public ResponseEntity<HomeHeroResponseDto> getActiveOne() {
        return ResponseEntity.ok(homeHeroService.getActiveOne());
    }

    @Operation(
            summary = "활성 히어로 리스트",
            description = "캐러셀 등에서 사용할 수 있는 다건 조회 (기본 5건)"
    )
    @GetMapping("/list")
    public ResponseEntity<List<HomeHeroResponseDto>> getActiveList(
            @Parameter(description = "최대 개수", example = "5")
            @RequestParam(defaultValue = "5") int limit
    ) {
        return ResponseEntity.ok(homeHeroService.getActiveList(limit));
    }
}
