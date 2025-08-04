package com.boot.loiteBackend.web.mileage.total.controller;

import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.web.mileage.total.dto.MileageTotalDto;
import com.boot.loiteBackend.web.mileage.total.service.MileageTotalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/private/mileage/total")
@Tag(name = "", description = "")
public class MileageTotalController {

    private final MileageTotalService mileageTotalService;

    @Operation(summary = "마일리지 조회", description = "로그인한 사용자의 총 보유 마일리지를 조회합니다.")
    @GetMapping
    public ResponseEntity<ApiResponse<MileageTotalDto>> getMyMileageTotal(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        MileageTotalDto dto = mileageTotalService.getMileageTotal(userDetails.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(dto, "마일리지 조회 성공"));
    }
}
