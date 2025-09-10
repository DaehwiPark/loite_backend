package com.boot.loiteBackend.web.home.recommend.section.controller;

import com.boot.loiteBackend.web.home.recommend.section.dto.HomeRecoSectionResponseDto;
import com.boot.loiteBackend.web.home.recommend.section.service.HomeRecoSectionService;
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
@RequestMapping("/api/public/reco/section")
@Tag(name = "메인 추천상품 API", description = "홈 추천 섹션 리스트 조회")
public class HomeRecoSectionController {

    private final HomeRecoSectionService homeRecoSectionService;

    @Operation(summary = "추천 상품 섹션 조회", description = "홈 화면에 노출할 추천 섹션 목록을 반환합니다.")
    @GetMapping
    public ResponseEntity<List<HomeRecoSectionResponseDto>> list() {
        var result = homeRecoSectionService.list();
        if (result.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(result);
    }
}
