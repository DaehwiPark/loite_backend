package com.boot.loiteBackend.web.payment.service;

import com.boot.loiteBackend.web.payment.dto.PaymentRequestDto;
import com.boot.loiteBackend.web.payment.dto.PaymentResponseDto;
import com.boot.loiteBackend.web.payment.dto.PaymentVerifyRequestDto;
import com.boot.loiteBackend.web.payment.dto.PaymentVerifyResponseDto;

public interface PaymentService {

    PaymentVerifyResponseDto verifyPayment(PaymentVerifyRequestDto requestDto);
}
