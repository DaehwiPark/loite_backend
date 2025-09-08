package com.boot.loiteBackend.admin.home.eventbanner.repository;

import com.boot.loiteBackend.domain.home.eventbanner.entity.HomeEventBannerEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface AdminHomeEventBannerRepository extends JpaRepository<HomeEventBannerEntity, Long>, JpaSpecificationExecutor<HomeEventBannerEntity> {

    /**
     * 같은 슬롯(DEFAULT_SLOT)의 기존 DEFAULT_YN='Y'를 모두 'N'으로 초기화.
     * excludeId가 있으면 해당 ID는 제외(수정 시 자기 자신 제외).
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE HomeEventBannerEntity b
           SET b.defaultYn = 'N'
         WHERE b.defaultYn = 'Y'
           AND b.defaultSlot = :slot
           AND (:excludeId IS NULL OR b.id <> :excludeId)
    """)
    int clearDefaultYnBySlot(@Param("slot") Integer slot, @Param("excludeId") Long excludeId);
}