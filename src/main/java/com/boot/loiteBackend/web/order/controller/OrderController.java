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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/private/orders")
@RequiredArgsConstructor
@Tag(name = "결제 관리 API", description = "결제 관련 기능 API")
public class OrderController {

    private final OrderService orderService;

    @Operation(summary = "주문서 생성", description = "결제할 상품의 주문서를 생성합니다.")
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@AuthenticationPrincipal CustomUserDetails loginUser, @RequestBody OrderRequestDto requestDto) {
        OrderResponseDto response = orderService.createOrder(loginUser.getUserId(), requestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "주문서 단건 조회", description = "로그인한 사용자의 단건 주문 내역을 조회합니다.")
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrder(@AuthenticationPrincipal CustomUserDetails loginUser, @PathVariable Long orderId) {
        OrderResponseDto response = orderService.getOrder(loginUser.getUserId(), orderId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "주문서 전체 조회", description = "로그인한 사용자의 모든 주문 내역을 조회합니다.")
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getOrders(@AuthenticationPrincipal CustomUserDetails loginUser) {
        List<OrderResponseDto> response = orderService.getOrders(loginUser.getUserId());
        return ResponseEntity.ok(response);
    }
}
