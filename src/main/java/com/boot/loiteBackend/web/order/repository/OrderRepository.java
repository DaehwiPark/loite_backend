package com.boot.loiteBackend.web.order.repository;

import com.boot.loiteBackend.web.order.dto.OrderResponseDto;
import com.boot.loiteBackend.web.order.entity.OrderEntity;
import com.boot.loiteBackend.web.order.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    Optional<OrderEntity>  findByOrderIdAndUserIdAndDeleteYn(Long orderId, Long userId , String deleteYn);

    Page<OrderEntity> findByUserIdAndDeleteYn(Long userId, String deleteYn, Pageable pageable);

    boolean existsByOrderIdAndUserIdAndOrderStatus(Long orderId, Long userId, OrderStatus status);
}
