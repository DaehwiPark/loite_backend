package com.boot.loiteBackend.web.home.mgazineBanner.controller;

import com.boot.loiteBackend.web.home.mgazineBanner.dto.HomeMagazineBannerDto;
import com.boot.loiteBackend.web.home.mgazineBanner.service.HomeMagazineBannerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/public/magazine-banner")
@Tag(name = "메인 캐러셀 배너 API", description = "쇼핑몰 캐러셀 배너(매거진 이동) 읽기 API")
public class HomeMagazineBannerController {

    private final HomeMagazineBannerService homeMagazineBannerService;

    @Operation(summary = "매거진 배너 전체 조회", description = "노출(Y)인 홈 매거진 배너 전체를 조회한다.")
    @GetMapping
    public ResponseEntity<List<HomeMagazineBannerDto>> getAll() {
        return ResponseEntity.ok(homeMagazineBannerService.getAllBanners());
    }
}