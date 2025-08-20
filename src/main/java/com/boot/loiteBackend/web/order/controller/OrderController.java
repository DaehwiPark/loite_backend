package com.boot.loiteBackend.web.order.controller;

import com.boot.loiteBackend.config.security.CustomUserDetails;
import com.boot.loiteBackend.web.order.dto.OrderRequestDto;
import com.boot.loiteBackend.web.order.dto.OrderResponseDto;
import com.boot.loiteBackend.web.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/private/orders")
@RequiredArgsConstructor
@Tag(name = "결제 관리 API", description = "결제 관련 기능 API")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문서 생성", description = "결제할 상품의 주문서를 생성합니다.")
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(
            @AuthenticationPrincipal CustomUserDetails loginUser, // 로그인된 사용자 ID 가져오기
            @RequestBody OrderRequestDto requestDto
    ) {
        OrderResponseDto response = orderService.createOrder(loginUser.getUserId(), requestDto);
        return ResponseEntity.ok(response);
    }
}
