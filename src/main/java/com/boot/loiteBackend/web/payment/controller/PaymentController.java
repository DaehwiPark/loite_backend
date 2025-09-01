package com.boot.loiteBackend.web.payment.controller;

import com.boot.loiteBackend.config.security.CustomUserDetails;
import com.boot.loiteBackend.web.payment.client.PortOneClient;
import com.boot.loiteBackend.web.payment.dto.PaymentRequestDto;
import com.boot.loiteBackend.web.payment.dto.PaymentResponseDto;
import com.boot.loiteBackend.web.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/api/private/payments")
@RequiredArgsConstructor
@Tag(name = "결제 API", description = "결제 정보 저장 및 조회를 관리합니다.")
public class PaymentController {

    private final PaymentService paymentService;
    private final PortOneClient portOneClient;

    @Operation(summary = "결제 정보 저장", description = "프론트에서 받은 결제 정보를 저장합니다.")
    @PostMapping
    public ResponseEntity<PaymentResponseDto> savePayment(@AuthenticationPrincipal CustomUserDetails loginUser, @RequestBody PaymentRequestDto requestDto) {
        PaymentResponseDto response = paymentService.verifyAndSavePayment(loginUser.getUserId(), requestDto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/token")
    public ResponseEntity<String> getToken() {
        String token = portOneClient.getAccessToken();
        return ResponseEntity.ok(token);
    }

    @GetMapping("/{txId}")
    public ResponseEntity<Map<String, Object>> testPayment(@PathVariable String txId) {
        Map<String, Object> paymentInfo = portOneClient.getPaymentByTxId(txId);
        return ResponseEntity.ok(paymentInfo);
    }
}
