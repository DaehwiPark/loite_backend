package com.boot.loiteBackend.web.payment.controller;

import com.boot.loiteBackend.config.security.CustomUserDetails;
import com.boot.loiteBackend.web.payment.client.PortOneClient;
import com.boot.loiteBackend.web.payment.dto.PaymentRequestDto;
import com.boot.loiteBackend.web.payment.dto.PaymentResponseDto;
import com.boot.loiteBackend.web.payment.dto.PaymentVerifyRequestDto;
import com.boot.loiteBackend.web.payment.dto.PaymentVerifyResponseDto;
import com.boot.loiteBackend.web.payment.service.PaymentService;
import com.boot.loiteBackend.web.payment.util.SignatureUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/private/payments")
@RequiredArgsConstructor
@Tag(name = "결제 API", description = "결제 정보 저장 및 조회를 관리합니다.")
public class PaymentController {

    private final PaymentService paymentService;
    private final PortOneClient portOneClient;
    private final SignatureUtil signatureUtil;
    private final ObjectMapper objectMapper;

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

    @Operation(summary = "웹훅 수신", description = "PortOne에서 결제 이벤트를 전달받아 처리합니다.")
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestHeader(value = "x-portone-signature", required = false) String signature, @RequestBody String rawBody) {
        if (signature == null) {
            log.warn("Webhook called without signature header! rawBody={}", rawBody);
            // 테스트 호출일 경우 그냥 통과시켜주고 싶으면 200 OK 리턴
            return ResponseEntity.ok("success");
        }
        try {
            // 1. 서명 검증
            if (!signatureUtil.verifySignature(rawBody, signature)) {
                log.warn("Invalid signature. rawBody={}, signature={}", rawBody, signature);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("invalid signature");
            }

            // 2. 로그 출력
            log.info("Webhook received. signature={}, body={}", signature, rawBody);

            // 3. JSON 파싱
            Map<String, Object> payload = objectMapper.readValue(rawBody, Map.class);

            // 4. 비즈니스 로직 실행
            paymentService.processWebhook(payload);

            return ResponseEntity.ok("success");
        } catch (Exception e) {
            log.error("Webhook processing failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("fail");
        }
    }
}
