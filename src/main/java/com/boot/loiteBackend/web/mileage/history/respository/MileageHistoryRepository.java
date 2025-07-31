package com.boot.loiteBackend.web.mileage.history.respository;

import com.boot.loiteBackend.web.mileage.history.entity.MileageHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MileageHistoryRepository extends JpaRepository<MileageHistoryEntity, Long> {

    List<MileageHistoryEntity> findByUserId(Long userId);

}