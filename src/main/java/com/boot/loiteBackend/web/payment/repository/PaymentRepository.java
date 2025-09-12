package com.boot.loiteBackend.web.payment.repository;

import com.boot.loiteBackend.web.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByMerchantUid(String orderNumber);

    @Query("select p from PaymentEntity p where p.order.orderId = :orderId")
    Optional<PaymentEntity> findByOrderId(@Param("orderId") Long orderId);
}
