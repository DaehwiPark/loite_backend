package com.boot.loiteBackend.web.mileage.total.respository;

import com.boot.loiteBackend.web.mileage.total.entity.MileageTotalEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MileageTotalRepository extends JpaRepository<MileageTotalEntity, Long> {

    @Modifying
    @Query("UPDATE MileageTotalEntity m SET m.mileageTotalAmount = m.mileageTotalAmount + :point, m.mileageTotalEarned = m.mileageTotalEarned + :point WHERE m.userId = :userId")
    int increaseMileageAtomic(@Param("userId") Long userId, @Param("point") int point);

    @Modifying
    @Query(value = "INSERT INTO tb_mileage_total (user_id, mileage_total_amount, mileage_total_earned, mileage_total_used, mileage_total_expired, created_at, updated_at) " +
            "VALUES (:userId, :point, :point, 0, 0, now(), now())", nativeQuery = true)
    void insertInitialMileage(@Param("userId") Long userId, @Param("point") int point);
}

