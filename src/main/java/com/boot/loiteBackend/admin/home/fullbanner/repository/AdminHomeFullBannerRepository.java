package com.boot.loiteBackend.admin.home.fullbanner.repository;

import com.boot.loiteBackend.domain.home.fullbanner.entity.HomeFullBannerEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AdminHomeFullBannerRepository extends JpaRepository<HomeFullBannerEntity, Long>, JpaSpecificationExecutor<HomeFullBannerEntity> {

    /**
     * 모든 배너의 DEFAULT_YN = 'N'
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("update HomeFullBannerEntity e set e.defaultYn='N', e.updatedBy=:actor where e.defaultYn='Y'")
    int clearDefaultAll(@Param("actor") Long actor);

    /**
     * 지정 ID 제외하고 DEFAULT_YN = 'N'
     */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query("update HomeFullBannerEntity e set e.defaultYn='N', e.updatedBy=:actor where e.defaultYn='Y' and e.id <> :id")
    int clearDefaultAllExcept(@Param("id") Long id, @Param("actor") Long actor);

}
