package com.boot.loiteBackend.web.home.eventbanner.repository;

import com.boot.loiteBackend.domain.home.eventbanner.entity.HomeEventBannerEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface HomeEventBannerRepository extends JpaRepository<HomeEventBannerEntity, Long> {

    // 활성 배너(노출/기간 충족) — 섹션별
    @Query("""
            select b
              from HomeEventBannerEntity b
             where upper(b.displayYn) = 'Y'
               and (:now is null or b.startAt is null or b.startAt <= :now)
               and (:now is null or b.endAt   is null or b.endAt   >= :now)
             order by b.defaultSlot asc nulls last, b.sortOrder asc, b.startAt desc, b.id desc
            """)
    List<HomeEventBannerEntity> findActiveBySection(@Param("section") String section,
                                                    @Param("now") LocalDateTime now,
                                                    Pageable pageable);
}