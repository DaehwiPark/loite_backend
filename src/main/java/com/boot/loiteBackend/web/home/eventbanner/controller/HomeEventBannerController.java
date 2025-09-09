package com.boot.loiteBackend.web.home.eventbanner.controller;

import com.boot.loiteBackend.web.home.eventbanner.dto.HomeEventBannerResponseDto;
import com.boot.loiteBackend.web.home.eventbanner.service.HomeEventBannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/banner")
@Tag(name = "이벤트 배너 API", description = "쇼핑몰 홈 이벤트 배너 조회")
public class HomeEventBannerController {

    private final HomeEventBannerService homeEventBannerService;

    @Operation(summary = "이벤트 활성 배너", description = "DISPLAY_YN='Y' + 기간 충족 + 정렬 우선순위")
    @GetMapping
    public ResponseEntity<List<HomeEventBannerResponseDto>> list(
            @Parameter(description = "최대 개수", example = "2")
            @RequestParam(defaultValue = "2") int limit
    ) {
        return ResponseEntity.ok(homeEventBannerService.getActiveBySection(limit));
    }
}
