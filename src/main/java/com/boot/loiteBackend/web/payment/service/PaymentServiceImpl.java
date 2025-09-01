package com.boot.loiteBackend.web.payment.service;

import com.boot.loiteBackend.web.order.entity.OrderEntity;
import com.boot.loiteBackend.web.order.enums.OrderStatus;
import com.boot.loiteBackend.web.order.repository.OrderRepository;
import com.boot.loiteBackend.web.payment.client.PortOneClient;
import com.boot.loiteBackend.web.payment.dto.PaymentRequestDto;
import com.boot.loiteBackend.web.payment.dto.PaymentResponseDto;
import com.boot.loiteBackend.web.payment.entity.PaymentEntity;
import com.boot.loiteBackend.web.payment.enums.PaymentStatus;
import com.boot.loiteBackend.web.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PortOneClient portOneClient;

    @Override
    @Transactional
    public PaymentResponseDto verifyAndSavePayment(Long userId, PaymentRequestDto requestDto) {
        // 1. 주문 찾기
        OrderEntity order = orderRepository.findByOrderNumber(requestDto.getMerchantUid())
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        if (!order.getUserId().equals(userId)) {
            throw new SecurityException("본인 주문만 결제할 수 있습니다.");
        }

        // 2. PortOne V2 API 호출
        Map<String, Object> result = portOneClient.getPaymentByTxId(requestDto.getTxId());
        Map<String, Object> paymentInfo = (Map<String, Object>) result.get("data");
        if (paymentInfo == null) {
            throw new IllegalStateException("PortOne 결제 응답이 올바르지 않습니다.");
        }

        // 3. PortOne 응답 파싱
        String txId = (String) paymentInfo.get("id"); // PortOne 트랜잭션 ID
        String pgProvider = (String) paymentInfo.get("pgProvider");
        String pgTid = (String) paymentInfo.get("pgTxId");
        String status = (String) paymentInfo.get("status"); // PAID, FAILED 등
        String receiptUrl = (String) paymentInfo.get("receiptUrl");

        // 금액
        Map<String, Object> amountMap = (Map<String, Object>) paymentInfo.get("amount");
        Integer paidAmount = (Integer) amountMap.get("total");

        // 결제수단
        Map<String, Object> methodMap = (Map<String, Object>) paymentInfo.get("method");
        String payMethod = (String) methodMap.get("type");

        // 4. 상태 매핑
        PaymentStatus paymentStatus = mapPortOneStatus(status);

        // 5. 금액 검증
        if (!order.getTotalAmount().equals(BigDecimal.valueOf(paidAmount))) {
            throw new IllegalStateException("결제 금액 불일치");
        }

        // 6. 결제 저장
        PaymentEntity payment = PaymentEntity.builder()
                .order(order)
                .merchantUid(order.getOrderNumber())
                .txId(txId) // ✅ V2 트랜잭션 ID
                .pgTid(pgTid)
                .pgProvider(pgProvider)
                .paymentMethod(payMethod)
                .paymentTotalAmount(BigDecimal.valueOf(paidAmount))
                .paymentAmountApproved(BigDecimal.valueOf(paidAmount))
                .paymentStatus(paymentStatus)
                .receiptUrl(receiptUrl)
                .rawPayload(paymentInfo.toString())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        // 주문 상태 업데이트
        order.setOrderStatus(OrderStatus.PAID);
        orderRepository.save(order);

        // 7. 응답 반환
        return PaymentResponseDto.builder()
                .paymentId(payment.getPaymentId())
                .merchantUid(payment.getMerchantUid())
                .txId(payment.getTxId())
                .paymentStatus(payment.getPaymentStatus())
                .paymentTotalAmount(payment.getPaymentTotalAmount())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    private PaymentStatus mapPortOneStatus(String status) {
        if (status == null) return PaymentStatus.FAILED;

        return switch (status.toUpperCase()) {
            case "READY" -> PaymentStatus.READY;
            case "PAID" -> PaymentStatus.PAID;
            case "CANCELED" -> PaymentStatus.CANCELED;
            case "PARTIAL_CANCELED" -> PaymentStatus.PARTIAL_CANCELED;
            case "FAILED" -> PaymentStatus.FAILED;
            case "PENDING" -> PaymentStatus.PENDING;
            default -> PaymentStatus.FAILED;
        };
    }
}

