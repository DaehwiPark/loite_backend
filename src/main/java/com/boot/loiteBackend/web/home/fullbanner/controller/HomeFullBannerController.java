package com.boot.loiteBackend.web.home.fullbanner.controller;

import com.boot.loiteBackend.web.home.fullbanner.dto.HomeFullBannerResponseDto;
import com.boot.loiteBackend.web.home.fullbanner.service.HomeFullBannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/full-banner")
@Tag(name = "웹 풀 배너 API", description = "대표 풀 배너 단건 조회")
public class HomeFullBannerController {

    private final HomeFullBannerService service;

    @Operation(summary = "대표 배너 조회", description = "DEFAULT_YN='Y'이고 노출중인 배너 1건 반환")
    @GetMapping
    public ResponseEntity<HomeFullBannerResponseDto> getDefaultActive() {
        return ResponseEntity.ok(service.getActiveDefault());
    }
}
