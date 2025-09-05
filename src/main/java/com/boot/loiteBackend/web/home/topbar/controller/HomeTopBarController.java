package com.boot.loiteBackend.web.home.topbar.controller;

import com.boot.loiteBackend.web.home.topbar.dto.HomeTopBarResponseDto;
import com.boot.loiteBackend.web.home.topbar.service.HomeTopBarService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/top-bar")
@Tag(name = "메인 탑바 API", description = "쇼핑몰 상단 (Top Bar) 조회")
public class HomeTopBarController {

    private final HomeTopBarService service;

    @Operation(summary = "활성 TopBar 단건", description = "DEFAULT_YN='Y' AND DISPLAY_YN='Y' 인 배너 1건 반환")
    @GetMapping
    public ResponseEntity<HomeTopBarResponseDto> getActive() {
        return ResponseEntity.ok(service.getActiveTopBar());
    }
}
