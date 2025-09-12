package com.boot.loiteBackend.web.payment.service;

import com.boot.loiteBackend.web.order.entity.OrderEntity;
import com.boot.loiteBackend.web.order.enums.OrderStatus;
import com.boot.loiteBackend.web.order.repository.OrderRepository;
import com.boot.loiteBackend.web.payment.client.PortOneClient;
import com.boot.loiteBackend.web.payment.dto.PaymentVerifyRequestDto;
import com.boot.loiteBackend.web.payment.dto.PaymentVerifyResponseDto;
import com.boot.loiteBackend.web.payment.entity.PaymentEntity;
import com.boot.loiteBackend.web.payment.enums.PaymentStatus;
import com.boot.loiteBackend.web.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PortOneClient portOneClient;

    @Override
    public PaymentVerifyResponseDto verifyPayment(PaymentVerifyRequestDto requestDto) {
        String paymentId = requestDto.getPaymentId(); // 주문번호 = PortOne paymentId

        // 1. 주문 조회
        OrderEntity order = orderRepository.findByOrderNumber(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없음: " + paymentId));

        // 2. 포트원 결제 단건 조회
        Map<String, Object> portOneRes = portOneClient.getPaymentByPaymentId(paymentId);

        // 3. 결제 금액 검증
        Map<String, Object> amount = (Map<String, Object>) portOneRes.get("amount");
        BigDecimal paidAmount = new BigDecimal(((Number) amount.get("total")).toString());

        if (order.getTotalAmount().compareTo(paidAmount) != 0) {
            // 금액 불일치 → 결제 실패 처리
            order.setOrderStatus(OrderStatus.PAYMENT_FAILED);
            orderRepository.save(order);

            return PaymentVerifyResponseDto.builder()
                    .orderId(order.getOrderId())
                    .orderNumber(order.getOrderNumber())
                    .paymentId(null)
                    .paymentStatus(PaymentStatus.FAILED)
                    .build();
        }

        // 4. 기존 결제 정보 확인 (주문번호 기준)
        Optional<PaymentEntity> existingOpt = paymentRepository.findByMerchantUid(order.getOrderNumber());

        PaymentEntity payment;

        String statusStr = (String) portOneRes.get("status");
        PaymentStatus status = PaymentStatus.valueOf(statusStr);

        // === method/provider 추출 ===
        Map<String, Object> methodMap = (Map<String, Object>) portOneRes.get("method");
        String provider = methodMap != null ? (String) methodMap.get("provider") : null; // KAKAOPAY, CARD 등
        String methodType = methodMap != null ? (String) methodMap.get("type") : null;   // PaymentMethodEasyPay 등 (참고용)

        if (existingOpt.isPresent()) {
            // 기존 결제 row 업데이트
            payment = existingOpt.get();
            payment.setTxId((String) portOneRes.get("transactionId"));
            payment.setPgProvider(((Map<String, Object>) portOneRes.get("channel")).get("pgProvider").toString());
            payment.setPaymentMethod(provider);   // ✅ provider 저장
            payment.setPaymentAmountApproved(paidAmount);
            payment.setPaymentStatus(status);
            payment.setReceiptUrl((String) portOneRes.get("receiptUrl"));
            payment.setRequestedAt(parseDateTime((String) portOneRes.get("requestedAt")));
            payment.setPaidAt(parseDateTime((String) portOneRes.get("paidAt")));
            payment.setRawPayload(portOneRes.toString());
        } else {
            // 새 결제 row insert
            payment = PaymentEntity.builder()
                    .order(order)
                    .merchantUid(order.getOrderNumber())
                    .txId((String) portOneRes.get("transactionId"))
                    .pgProvider(((Map<String, Object>) portOneRes.get("channel")).get("pgProvider").toString())
                    .paymentMethod(provider)
                    .paymentTotalAmount(order.getTotalAmount())
                    .paymentAmountApproved(paidAmount)
                    .paymentCurrency((String) portOneRes.getOrDefault("currency", "KRW"))
                    .paymentStatus(status)
                    .receiptUrl((String) portOneRes.get("receiptUrl"))
                    .requestedAt(parseDateTime((String) portOneRes.get("requestedAt")))
                    .paidAt(parseDateTime((String) portOneRes.get("paidAt")))
                    .rawPayload(portOneRes.toString())
                    .build();

            paymentRepository.save(payment);
        }

        // 5. 주문 상태 업데이트
        order.setOrderStatus(status == PaymentStatus.PAID ? OrderStatus.PAID : OrderStatus.PAYMENT_FAILED);
        orderRepository.save(order);

        // 6. 응답 변환 (한글 변환 포함)
        return PaymentVerifyResponseDto.builder()
                .orderId(order.getOrderId())
                .orderNumber(order.getOrderNumber())
                .paymentId(payment.getPaymentId())
                .paymentStatus(status)
                .paymentMethod(translatePaymentMethod(payment.getPaymentMethod()))
                .build();
    }

    private String translatePaymentMethod(String provider) {
        if (provider == null) return null;
        return switch (provider.toUpperCase()) {
            case "KAKAOPAY" -> "카카오페이";
            case "NAVERPAY" -> "네이버페이";
            case "CARD" -> "신용/체크카드";
            case "VIRTUAL_ACCOUNT" -> "가상계좌";
            case "TRANSFER" -> "계좌이체";
            default -> provider; // 정의 안 된 건 그대로
        };
    }

    @Override
    public void processWebhook(Map<String, Object> payload) {
        String orderNumber = (String) payload.get("id");
        OrderEntity order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없음: " + orderNumber));

        String statusStr = (String) payload.get("status");
        PaymentStatus status = PaymentStatus.valueOf(statusStr);

        Map<String, Object> method = (Map<String, Object>) payload.get("method");
        Map<String, Object> channel = (Map<String, Object>) payload.get("channel");
        Map<String, Object> amount = (Map<String, Object>) payload.get("amount");

        String pgProvider = channel != null ? (String) channel.get("pgProvider") : null;
        String provider = method != null ? (String) method.get("provider") : null;

        BigDecimal paidAmount = amount != null
                ? new BigDecimal(((Number) amount.get("total")).toString())
                : BigDecimal.ZERO;

        PaymentEntity payment = paymentRepository.findByOrder(order)
                .orElse(PaymentEntity.builder().order(order).merchantUid(order.getOrderNumber()).build());

        payment.setTxId((String) payload.get("transactionId"));
        payment.setPgProvider(pgProvider);
        payment.setPaymentMethod(provider);
        payment.setPaymentTotalAmount(order.getTotalAmount());
        payment.setPaymentAmountApproved(paidAmount);
        payment.setPaymentCurrency((String) payload.getOrDefault("currency", "KRW"));
        payment.setPaymentStatus(status);
        payment.setReceiptUrl((String) payload.get("receiptUrl"));
        payment.setRequestedAt(parseDateTime((String) payload.get("requestedAt")));
        payment.setPaidAt(parseDateTime((String) payload.get("paidAt")));
        payment.setRawPayload(payload.toString());

        paymentRepository.save(payment);

        // 주문 상태 갱신
        if (status == PaymentStatus.PAID) {
            order.setOrderStatus(OrderStatus.PAID);
        } else if (status == PaymentStatus.CANCELLED) {
            order.setOrderStatus(OrderStatus.CANCELLED);
        } else if (status == PaymentStatus.FAILED) {
            order.setOrderStatus(OrderStatus.PAYMENT_FAILED);
        }
        orderRepository.save(order);
    }

    private LocalDateTime parseDateTime(String isoTime) {
        if (isoTime == null) return null;
        return LocalDateTime.parse(isoTime, DateTimeFormatter.ISO_DATE_TIME);
    }
}


