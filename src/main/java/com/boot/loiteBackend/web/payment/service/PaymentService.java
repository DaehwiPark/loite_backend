package com.boot.loiteBackend.web.payment.service;

import com.boot.loiteBackend.web.payment.dto.PaymentRequestDto;
import com.boot.loiteBackend.web.payment.dto.PaymentResponseDto;

public interface PaymentService {
    PaymentResponseDto verifyAndSavePayment(Long userId, PaymentRequestDto requestDto);
}
