package com.boot.loiteBackend.web.order.repository;

import com.boot.loiteBackend.web.order.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
}
