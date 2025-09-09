package com.boot.loiteBackend.admin.mainpage.popup.repository;

import com.boot.loiteBackend.domain.mainpage.popup.MainpagePopupEntity;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminMainpagePopupRepository extends JpaRepository<MainpagePopupEntity, Long> {

    /* 1) 현재 시각 기준 노출 대상(활성 + 기간 조건) 슬라이드 목록 */
    @Query("""
           select p
           from MainpagePopupEntity p
           where p.popupIsActive = true
             and (p.popupStartAt is null or p.popupStartAt <= :now)
             and (p.popupEndAt   is null or p.popupEndAt   >= :now)
           order by p.popupSortOrder asc, p.createdAt asc
           """)
    List<MainpagePopupEntity> findVisible(@Param("now") LocalDateTime now);

    /* 2) 단순 전체(활성만) 정렬 조회 – 어드민 화면 리스트용 */
    List<MainpagePopupEntity> findAllByPopupIsActiveTrueOrderByPopupSortOrderAscCreatedAtAsc();

    /* 3) 다음 정렬 값 계산용: 활성 중 최대 sort_order (없으면 0) */
    @Query("select coalesce(max(p.popupSortOrder), 0) from MainpagePopupEntity p where p.popupIsActive = true")
    int findMaxSortOrderOfActive();

    /* 4) 정렬값 개별 업데이트 (드래그앤드롭 후 서비스에서 루프 호출) */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update MainpagePopupEntity p set p.popupSortOrder = :order where p.popupId = :id")
    int updateSortOrder(@Param("id") Long popupId, @Param("order") int sortOrder);

    /* 5) 활성/비활성 일괄 변경 */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update MainpagePopupEntity p set p.popupIsActive = :active where p.popupId in :ids")
    int bulkUpdateActive(@Param("ids") List<Long> ids, @Param("active") boolean active);

    /* 6) 소프트 삭제(표시 끄고 삭제시각 기록) */
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update MainpagePopupEntity p set p.popupIsActive = false, p.popupDeletedAt = current timestamp where p.popupId = :id")
    int softDelete(@Param("id") Long popupId);

    /* 7) 재정렬 시 동시성 안전하게 잠그고 읽고 싶을 때(선택) */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from MainpagePopupEntity p where p.popupId in :ids")
    List<MainpagePopupEntity> lockForReorder(@Param("ids") List<Long> ids);
}
