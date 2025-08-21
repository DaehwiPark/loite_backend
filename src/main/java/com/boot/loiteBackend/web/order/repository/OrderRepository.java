package com.boot.loiteBackend.web.order.repository;

import com.boot.loiteBackend.web.order.dto.OrderResponseDto;
import com.boot.loiteBackend.web.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    Optional<OrderEntity> findByOrderIdAndUserId(Long orderId, Long userId);

    List<OrderEntity> findByUserId(Long userId);
}
