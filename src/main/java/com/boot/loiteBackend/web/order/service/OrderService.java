package com.boot.loiteBackend.web.order.service;


import com.boot.loiteBackend.web.order.dto.OrderRequestDto;
import com.boot.loiteBackend.web.order.dto.OrderResponseDto;

import java.util.List;

public interface OrderService {
    OrderResponseDto createOrder(Long userId, OrderRequestDto dto);

    OrderResponseDto getOrder(Long orderId, Long userId);

    List<OrderResponseDto> getOrders(Long userId);
}
