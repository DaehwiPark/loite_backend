package com.boot.loiteBackend.admin.home.recommend.section.repository;

import com.boot.loiteBackend.domain.home.recommend.section.entity.HomeRecoSectionEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface AdminHomeRecoSectionRepository
        extends JpaRepository<HomeRecoSectionEntity, Long>, JpaSpecificationExecutor<HomeRecoSectionEntity> {

    /** 현재 최대 sort_order (없으면 null) */
    @Query("select max(s.sortOrder) from HomeRecoSectionEntity s")
    Integer findMaxSortOrder();

    /** 새 섹션을 pos 위치에 끼워넣을 때, pos 이상 요소들 +1 (전부 뒤로 밀기) */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update HomeRecoSectionEntity s set s.sortOrder = s.sortOrder + 1 where s.sortOrder >= :pos")
    int shiftBackFrom(@Param("pos") int pos);

    /** 정렬 이동: 위로 당김 (newPos < oldPos) → [newPos, oldPos-1] 구간 +1 (뒤로 밀기) */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
           update HomeRecoSectionEntity s
              set s.sortOrder = s.sortOrder + 1
            where s.sortOrder >= :newPos and s.sortOrder < :oldPos and s.id <> :id
           """)
    int shiftRangeUp(@Param("id") Long id, @Param("newPos") int newPos, @Param("oldPos") int oldPos);

    /** 정렬 이동: 아래로 내림 (newPos > oldPos) → [oldPos+1, newPos] 구간 -1 (앞으로 당기기) */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
           update HomeRecoSectionEntity s
              set s.sortOrder = s.sortOrder - 1
            where s.sortOrder <= :newPos and s.sortOrder > :oldPos and s.id <> :id
           """)
    int shiftRangeDown(@Param("id") Long id, @Param("oldPos") int oldPos, @Param("newPos") int newPos);
}
