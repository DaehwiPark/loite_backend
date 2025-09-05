package com.boot.loiteBackend.web.home.fullbanner.repository;

import com.boot.loiteBackend.domain.home.fullbanner.entity.HomeFullBannerEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface HomeFullBannerRepository extends JpaRepository<HomeFullBannerEntity, Long> {

    /**
     * 대표(Y) + 노출(Y) + 기간(now 포함) 조건의 배너를 정렬과 함께 조회.
     * Pageable 로 top1만 요청해서 사용.
     */
    @Query("""
        select e
          from HomeFullBannerEntity e
         where upper(e.displayYn) = 'Y'
           and upper(e.defaultYn) = 'Y'
           and (e.startAt is null or e.startAt <= :now)
           and (e.endAt   is null or e.endAt   >= :now)
        """)
    List<HomeFullBannerEntity> findActiveDefaultForNow(@Param("now") LocalDateTime now, Pageable pageable);
}
