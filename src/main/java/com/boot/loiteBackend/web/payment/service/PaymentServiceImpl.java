package com.boot.loiteBackend.web.payment.service;

import com.boot.loiteBackend.web.order.entity.OrderEntity;
import com.boot.loiteBackend.web.order.repository.OrderRepository;
import com.boot.loiteBackend.web.payment.dto.PaymentRequestDto;
import com.boot.loiteBackend.web.payment.dto.PaymentResponseDto;
import com.boot.loiteBackend.web.payment.entity.PaymentEntity;
import com.boot.loiteBackend.web.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public PaymentResponseDto verifyAndSavePayment(Long userId, PaymentRequestDto requestDto) {
        // 1. 주문 찾기 (merchantUid 기반)
        OrderEntity order = orderRepository.findByOrderNumber(requestDto.getMerchantUid())
                .orElseThrow(() -> new IllegalArgumentException("주문을 찾을 수 없습니다."));

        if (!order.getUserId().equals(userId)) {
            throw new SecurityException("본인 주문만 결제할 수 있습니다.");
        }

        // 2. PaymentEntity 생성
        PaymentEntity payment = PaymentEntity.builder()
                .order(order)
                .merchantUid(requestDto.getMerchantUid())
                .impUid(requestDto.getImpUid())
                .pgProvider(requestDto.getPgProvider())
                .paymentMethod(requestDto.getPaymentMethod())
                .paymentTotalAmount(requestDto.getPaymentTotalAmount())
                .paymentAmountApproved(BigDecimal.ZERO) // 승인된 금액, 추후 포트원 API 검증 시 업데이트
                .paymentStatus(requestDto.getPaymentStatus())
                .rawPayload(requestDto.getRawPayLoad())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        // 3. ResponseDto 리턴
        return PaymentResponseDto.builder()
                .paymentId(payment.getPaymentId())
                .merchantUid(payment.getMerchantUid())
                .impUid(payment.getImpUid())
                .paymentStatus(payment.getPaymentStatus())
                .paymentTotalAmount(payment.getPaymentTotalAmount())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}
