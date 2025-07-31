package com.boot.loiteBackend.web.mileage.history.controller;

import com.boot.loiteBackend.global.response.ApiResponse;
import com.boot.loiteBackend.global.security.CustomUserDetails;
import com.boot.loiteBackend.web.mileage.history.dto.MileageHistoryDto;
import com.boot.loiteBackend.web.mileage.history.service.MileageHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/private/mileage/history")
@Tag(name = "마일리지 이력", description = "마일리지 적립/사용/소멸 내역 관리 API")
public class MileageHistoryController {

    private final MileageHistoryService mileageHistoryService;

//    @PostMapping
//    @Operation(summary = "마일리지 등록", description = "EARN / USE / EXPIRE 타입으로 마일리지를 등록합니다.")
//    public ResponseEntity<ApiResponse<Void>> register(
//            @AuthenticationPrincipal CustomUserDetails loginUser,
//            @RequestBody MileageHistoryDto dto
//    ) {
//        dto.setUserId(loginUser.getUserId());
//        mileageHistoryService.addHistory(dto);
//        return ResponseEntity.ok(ApiResponse.ok("마일리지가 등록되었습니다."));
//    }

    @GetMapping
    @Operation(summary = "마일리지 이력 조회", description = "현재 로그인한 회원의 마일리지 적립/사용/소멸 내역을 조회합니다.")
    public ResponseEntity<ApiResponse<List<MileageHistoryDto>>> getHistory(
            @AuthenticationPrincipal CustomUserDetails loginUser
    ) {
        List<MileageHistoryDto> historyList = mileageHistoryService.getHistories(loginUser.getUserId());
        return ResponseEntity.ok(ApiResponse.ok(historyList));
    }

}