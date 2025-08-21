package com.boot.loiteBackend.web.order.repository;

import com.boot.loiteBackend.web.order.entity.OrderSequenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface OrderSequenceRepository extends JpaRepository<OrderSequenceEntity, Long> {

    @Query(value = "INSERT INTO tb_order_sequence (seq_date) VALUES (?1)", nativeQuery = true)
    @Modifying
    @Transactional
    void insertSeq(String seqDate);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastSeq();
}
