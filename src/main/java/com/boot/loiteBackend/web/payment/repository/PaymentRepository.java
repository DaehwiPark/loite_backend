package com.boot.loiteBackend.web.payment.repository;

import com.boot.loiteBackend.web.payment.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {
    Optional<PaymentEntity> findByMerchantUid(String orderNumber);
}
