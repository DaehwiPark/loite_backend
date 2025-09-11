package com.boot.loiteBackend.admin.home.best.repository;

import com.boot.loiteBackend.domain.home.best.entity.HomeBestItemEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminHomeBestItemRepository
        extends JpaRepository<HomeBestItemEntity, Long>, JpaSpecificationExecutor<HomeBestItemEntity> {

    // 뒤로 밀 항목들 잠금 (전역)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<HomeBestItemEntity> findBySlotNoGreaterThanEqualOrderBySlotNoDesc(Integer slotNo);

    // 전역 개수
    @Query("select count(e) from HomeBestItemEntity e")
    int countAll();

    // 최대 슬롯
    @Query("select max(e.slotNo) from HomeBestItemEntity e")
    Integer findMaxSlotNo();

    // 오른쪽(+1): [start..end] 범위, 대상 제외 (전역)
    @Modifying(flushAutomatically = true, clearAutomatically = false)
    @Query(value = """
            UPDATE tb_home_best_item
               SET HOME_BEST_ITEM_SLOT_NO = HOME_BEST_ITEM_SLOT_NO + 1
             WHERE HOME_BEST_ITEM_ID <> :id
               AND HOME_BEST_ITEM_SLOT_NO BETWEEN :start AND :end
            """, nativeQuery = true)
    int shiftRangeRightByOneExcludingId(@Param("id") Long excludeItemId,
                                        @Param("start") int start,
                                        @Param("end") int end);

    // 왼쪽(-1): [start..end] 범위, 대상 제외 (전역)
    @Modifying(flushAutomatically = true, clearAutomatically = false)
    @Query(value = """
            UPDATE tb_home_best_item
               SET HOME_BEST_ITEM_SLOT_NO = HOME_BEST_ITEM_SLOT_NO - 1
             WHERE HOME_BEST_ITEM_ID <> :id
               AND HOME_BEST_ITEM_SLOT_NO BETWEEN :start AND :end
            """, nativeQuery = true)
    int shiftRangeLeftByOneExcludingId(@Param("id") Long excludeItemId,
                                       @Param("start") int start,
                                       @Param("end") int end);

    // 대상 슬롯 지정 (전역)
    @Modifying(flushAutomatically = true, clearAutomatically = false)
    @Query(value = """
            UPDATE tb_home_best_item
               SET HOME_BEST_ITEM_SLOT_NO = :newPos
             WHERE HOME_BEST_ITEM_ID = :id
            """, nativeQuery = true)
    int updateSlot(@Param("id") Long itemId, @Param("newPos") int newPos);

    // desired..10 중 첫 빈 슬롯 (전역)
    @Query(value = """
            SELECT MIN(t.n) AS free_slot
              FROM (
                    SELECT :desired AS n
                    UNION ALL SELECT :desired+1
                    UNION ALL SELECT :desired+2
                    UNION ALL SELECT :desired+3
                    UNION ALL SELECT :desired+4
                    UNION ALL SELECT :desired+5
                    UNION ALL SELECT :desired+6
                    UNION ALL SELECT :desired+7
                    UNION ALL SELECT :desired+8
                    UNION ALL SELECT :desired+9
                   ) t
              LEFT JOIN tb_home_best_item i
                ON i.HOME_BEST_ITEM_SLOT_NO = t.n
             WHERE t.n BETWEEN :desired AND 10
               AND i.HOME_BEST_ITEM_ID IS NULL
            """, nativeQuery = true)
    Integer findFirstFreeSlotFrom(@Param("desired") int desired);

    // 1..upto 중 마지막(가장 큰) 빈 슬롯 (전역)
    @Query(value = """
            SELECT MAX(t.n) AS free_slot
              FROM (
                    SELECT 1 AS n
                    UNION ALL SELECT 2
                    UNION ALL SELECT 3
                    UNION ALL SELECT 4
                    UNION ALL SELECT 5
                    UNION ALL SELECT 6
                    UNION ALL SELECT 7
                    UNION ALL SELECT 8
                    UNION ALL SELECT 9
                    UNION ALL SELECT 10
                   ) t
              LEFT JOIN tb_home_best_item i
                ON i.HOME_BEST_ITEM_SLOT_NO = t.n
             WHERE t.n BETWEEN 1 AND :upto
               AND i.HOME_BEST_ITEM_ID IS NULL
            """, nativeQuery = true)
    Integer findLastFreeSlotUpTo(@Param("upto") int upto);

    //생성 시: [start..end] +1 (전역)
    @Modifying(flushAutomatically = true, clearAutomatically = false)
    @Query(value = """
            UPDATE tb_home_best_item
               SET HOME_BEST_ITEM_SLOT_NO = HOME_BEST_ITEM_SLOT_NO + 1
             WHERE HOME_BEST_ITEM_SLOT_NO BETWEEN :start AND :end
            """, nativeQuery = true)
    int shiftRangeRightByOne(@Param("start") int start,
                             @Param("end") int end);

    // 생성 시: [start..end] -1 (전역, 제외 없음)
    @Modifying(flushAutomatically = true, clearAutomatically = false)
    @Query(value = """
            UPDATE tb_home_best_item
               SET HOME_BEST_ITEM_SLOT_NO = HOME_BEST_ITEM_SLOT_NO - 1
             WHERE HOME_BEST_ITEM_SLOT_NO BETWEEN :start AND :end
            """, nativeQuery = true)
    int shiftRangeLeftByOne(@Param("start") int start,
                            @Param("end") int end);

}
