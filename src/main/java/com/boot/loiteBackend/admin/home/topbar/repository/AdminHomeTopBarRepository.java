package com.boot.loiteBackend.admin.home.topbar.repository;

import com.boot.loiteBackend.domain.home.topbar.entity.HomeTopBarEntity;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AdminHomeTopBarRepository extends JpaRepository<HomeTopBarEntity, Long> {

    /** 현재 default=Y 인 단일 레코드(전역 스코프) */
    @Query("select t from HomeTopBarEntity t where upper(t.defaultYn) = 'Y'")
    Optional<HomeTopBarEntity> findCurrentDefault();

    /** 전부 N 으로 초기화 (전역 스코프). updatedBy를 기록하고 싶으면 userId 전달 */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update HomeTopBarEntity t " +
            "set t.defaultYn = 'N', t.updatedBy = :userId " +
            "where upper(t.defaultYn) = 'Y'")
    int clearDefaultAll(@Param("userId") Long userId);

    /** 본인(excludeId) 제외 전부 N (전역 스코프). 유니크 충돌 방지용 */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update HomeTopBarEntity t " +
            "set t.defaultYn = 'N', t.updatedBy = :userId " +
            "where upper(t.defaultYn) = 'Y' " +
            "and t.id <> :excludeId")
    int clearDefaultAllExcept(@Param("excludeId") Long excludeId, @Param("userId") Long userId);

    /* ──────────────────────────────────────────────────────────────
       🔽사용자 스코프가 필요하면 아래 두 메서드로 교체해서 사용하세요.

    @Query("select t from HomeTopBarEntity t where upper(t.defaultYn)='Y' and t.createdBy = :userId")
    Optional<HomeTopBarEntity> findCurrentDefaultByUser(@Param("userId") Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update HomeTopBarEntity t set t.defaultYn='N', t.updatedBy=:userId " +
           "where upper(t.defaultYn)='Y' and t.createdBy = :userId and t.id <> :excludeId")
    int clearDefaultAllExceptByUser(@Param("excludeId") Long excludeId, @Param("userId") Long userId);
    ─────────────────────────────────────────────────────────────── */
}
