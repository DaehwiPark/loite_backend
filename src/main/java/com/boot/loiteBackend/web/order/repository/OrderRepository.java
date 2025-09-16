package com.boot.loiteBackend.web.order.repository;

import com.boot.loiteBackend.web.order.dto.OrderResponseDto;
import com.boot.loiteBackend.web.order.entity.OrderEntity;
import com.boot.loiteBackend.web.order.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    Optional<OrderEntity> findByOrderNumber(String orderNumber);

    Optional<OrderEntity> findByOrderIdAndUserIdAndDeleteYn(Long orderId, Long userId, String deleteYn);

    Page<OrderEntity> findByUserIdAndDeleteYnAndOrderStatusNot(
            Long userId,
            String deleteYn,
            OrderStatus excludedStatus,
            Pageable pageable
    );


    boolean existsByOrderIdAndUserIdAndOrderStatus(Long orderId, Long userId, OrderStatus status);

    @Query(value = "SELECT o.ORDER_ID " +
            "FROM tb_order o " +
            "JOIN tb_order_item oi ON oi.ORDER_ID = o.ORDER_ID " +
            "WHERE o.USER_ID = :userId " +
            "AND oi.PRODUCT_ID = :productId " +
            "AND o.ORDER_STATUS = :#{#status.name()} " +
            "ORDER BY o.CREATED_AT DESC " +
                    "LIMIT 1",
    nativeQuery = true)
    Optional<Long> findMostRecentPaidOrderIdByUserIdAndProductId(
            @Param("userId") Long userId,
            @Param("productId") Long productId,
            @Param("status") OrderStatus status
    );
}

