package com.boot.loiteBackend.web.payment.controller;

import com.boot.loiteBackend.config.security.CustomUserDetails;
import com.boot.loiteBackend.web.payment.client.PortOneClient;
import com.boot.loiteBackend.web.payment.dto.PaymentRequestDto;
import com.boot.loiteBackend.web.payment.dto.PaymentResponseDto;
import com.boot.loiteBackend.web.payment.dto.PaymentVerifyRequestDto;
import com.boot.loiteBackend.web.payment.dto.PaymentVerifyResponseDto;
import com.boot.loiteBackend.web.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/private/payments")
@RequiredArgsConstructor
@Tag(name = "결제 API", description = "결제 정보 저장 및 조회를 관리합니다.")
public class PaymentController {

    private final PaymentService paymentService;
    private final PortOneClient portOneClient;

    @Operation(summary = "결제 정보 검증", description = "프론트에서 받은 결제 정보를 검증 및 저장합니다.")
    @PostMapping("/verify")
    public ResponseEntity<PaymentVerifyResponseDto> verifyPayment(
            @RequestBody PaymentVerifyRequestDto requestDto) {

        PaymentVerifyResponseDto response = paymentService.verifyPayment(requestDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "토큰 발급 테스트", description = "ㅇㅇㅇ")
    @GetMapping("/token")
    public ResponseEntity<String> getToken() {
        String token = portOneClient.getAccessToken();
        return ResponseEntity.ok(token);
    }

    @Operation(summary = "결제 단건 조회 테스트", description = "PortOne 결제 단건 조회 API 호출")
    @GetMapping("/{paymentId}")
    public ResponseEntity<Map<String, Object>> testPayment(@PathVariable String paymentId) {
        Map<String, Object> paymentInfo = portOneClient.getPaymentByPaymentId(paymentId);
        return ResponseEntity.ok(paymentInfo);
    }
}
