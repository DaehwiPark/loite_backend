package com.boot.loiteBackend.web.order.repository;

import com.boot.loiteBackend.web.order.entity.OrderEntity;
import com.boot.loiteBackend.web.order.entity.OrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItemEntity, Long> {
    List<OrderItemEntity> findByOrder(OrderEntity order);

    @Query("SELECT oi FROM OrderItemEntity oi " +
            "WHERE oi.order.orderId = :orderId " +
            "AND oi.product.productId = :productId")
    Optional<OrderItemEntity> findByOrderIdAndProductId(@Param("orderId") Long orderId, @Param("productId") Long productId);

}
