package com.boot.loiteBackend.web.order.service;


import com.boot.loiteBackend.web.order.dto.OrderRequestDto;
import com.boot.loiteBackend.web.order.dto.OrderResponseDto;
import org.springframework.transaction.annotation.Transactional;

public interface OrderService {
    @Transactional
    OrderResponseDto createOrder(Long userId, OrderRequestDto dto);
}
