package com.boot.loiteBackend.web.payment.service;

import com.boot.loiteBackend.web.order.entity.OrderEntity;
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
public class PaymentServiceImpl implements PaymentService{

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

        // 2. PortOne API 호출 (txId 기반 결제 정보 조회)
        Map<String, Object> result = portOneClient.getPaymentByTxId(requestDto.getTxId());
        Map<String, Object> paymentInfo = (Map<String, Object>) result.get("response");
        if (paymentInfo == null) {
            throw new IllegalStateException("PortOne 결제 응답이 올바르지 않습니다.");
        }

        String impUid = (String) paymentInfo.get("imp_uid");
        String pgProvider = (String) paymentInfo.get("pg_provider");
        String payMethod = (String) paymentInfo.get("pay_method");
        Integer paidAmount = (Integer) paymentInfo.get("amount");
        String status = (String) paymentInfo.get("status"); // paid, failed 등

        PaymentStatus paymentStatus = mapPortOneStatus(status);

        // 3. 결제 저장
        PaymentEntity payment = PaymentEntity.builder()
                .order(order)
                .merchantUid(order.getOrderNumber())
                .impUid(impUid)
                .pgProvider(pgProvider)
                .paymentMethod(payMethod)
                .paymentTotalAmount(BigDecimal.valueOf(paidAmount))
                .paymentAmountApproved(BigDecimal.valueOf(paidAmount))
                .paymentStatus(paymentStatus)
                .rawPayload(paymentInfo.toString())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        return PaymentResponseDto.builder()
                .paymentId(payment.getPaymentId())
                .merchantUid(payment.getMerchantUid())
                .impUid(payment.getImpUid())
                .paymentStatus(payment.getPaymentStatus())
                .paymentTotalAmount(payment.getPaymentTotalAmount())
                .createdAt(payment.getCreatedAt())
                .build();
    }

    private PaymentStatus mapPortOneStatus(String status) {
        if (status == null) return PaymentStatus.FAILED;

        return switch (status.toLowerCase()) {
            case "ready" -> PaymentStatus.READY;
            case "paid" -> PaymentStatus.PAID;
            case "cancelled" -> PaymentStatus.CANCELLED;
            case "partial_cancelled" -> PaymentStatus.PARTIAL_CANCELLED;
            case "failed" -> PaymentStatus.FAILED;
            case "pending" -> PaymentStatus.PENDING;
            default -> PaymentStatus.FAILED; // 알 수 없는 상태는 실패 처리
        };
    }
}
