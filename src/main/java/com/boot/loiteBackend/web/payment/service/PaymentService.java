package com.boot.loiteBackend.web.payment.service;

import com.boot.loiteBackend.web.payment.dto.PaymentRequestDto;
import com.boot.loiteBackend.web.payment.dto.PaymentResponseDto;
import com.boot.loiteBackend.web.payment.dto.PaymentVerifyRequestDto;
import com.boot.loiteBackend.web.payment.dto.PaymentVerifyResponseDto;

import java.util.Map;

public interface PaymentService {

    PaymentVerifyResponseDto verifyPayment(PaymentVerifyRequestDto requestDto);

    void processWebhook(Map<String, Object> payload);
}
