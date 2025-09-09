package com.boot.loiteBackend.admin.home.recommend.item.repository;

import com.boot.loiteBackend.domain.home.recommend.item.entity.HomeRecoItemEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AdminHomeRecoItemRepository
        extends JpaRepository<HomeRecoItemEntity, Long>, JpaSpecificationExecutor<HomeRecoItemEntity> {

    int countBySectionId(Long sectionId);

    @Query("select max(i.slotNo) from HomeRecoItemEntity i where i.sectionId = :sectionId")
    Integer findMaxSlotNo(@Param("sectionId") Long sectionId);

    /* (옵션) 동시성 방지: 새 삽입 전에 뒤로 밀 항목들 잡아둘 때 */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<HomeRecoItemEntity> findBySectionIdAndSlotNoGreaterThanEqualOrderBySlotNoDesc(Long sectionId, Integer slotNo);

    /**
     * 오른쪽으로 이동(+1): [start..end] 범위, 대상 아이템 제외
     */
    @Modifying(flushAutomatically = true, clearAutomatically = false)
    @Query(value = """
            UPDATE tb_home_reco_item
               SET home_reco_item_slot_no = home_reco_item_slot_no + 1
             WHERE home_reco_section_id = :sectionId
               AND home_reco_item_id <> :id
               AND home_reco_item_slot_no BETWEEN :start AND :end
             ORDER BY home_reco_item_slot_no DESC
            """, nativeQuery = true)
    int shiftRangeRightByOneExcludingId(@Param("sectionId") Long sectionId,
                                        @Param("id") Long excludeItemId,
                                        @Param("start") int start,
                                        @Param("end") int end);

    /**
     * 왼쪽으로 이동(-1): [start..end] 범위, 대상 아이템 제외
     */
    @Modifying(flushAutomatically = true, clearAutomatically = false)
    @Query(value = """
            UPDATE tb_home_reco_item
               SET home_reco_item_slot_no = home_reco_item_slot_no - 1
             WHERE home_reco_section_id = :sectionId
               AND home_reco_item_id <> :id
               AND home_reco_item_slot_no BETWEEN :start AND :end
             ORDER BY home_reco_item_slot_no ASC
            """, nativeQuery = true)
    int shiftRangeLeftByOneExcludingId(@Param("sectionId") Long sectionId,
                                       @Param("id") Long excludeItemId,
                                       @Param("start") int start,
                                       @Param("end") int end);

    /**
     * 대상 아이템 슬롯을 지정 위치로 세팅
     */
    @Modifying(flushAutomatically = true, clearAutomatically = false)
    @Query(value = """
            UPDATE tb_home_reco_item
               SET home_reco_item_slot_no = :newPos
             WHERE home_reco_item_id = :id
            """, nativeQuery = true)
    int updateSlot(@Param("id") Long itemId, @Param("newPos") int newPos);

    /**
     * 생성 시, desired..10 중 최초 빈 슬롯 찾기 (있으면 that; 없으면 null)
     */
    @Query(value = """
            SELECT MIN(t.n) AS free_slot
            FROM (
              SELECT  :desired AS n UNION ALL SELECT :desired+1 UNION ALL SELECT :desired+2 UNION ALL SELECT :desired+3 UNION ALL SELECT :desired+4
              UNION ALL SELECT :desired+5 UNION ALL SELECT :desired+6 UNION ALL SELECT :desired+7 UNION ALL SELECT :desired+8 UNION ALL SELECT :desired+9
            ) t
            LEFT JOIN tb_home_reco_item i
              ON i.home_reco_section_id = :sectionId AND i.home_reco_item_slot_no = t.n
            WHERE t.n BETWEEN :desired AND 10
              AND i.home_reco_item_id IS NULL
            """, nativeQuery = true)
    Integer findFirstFreeSlotFrom(@Param("sectionId") Long sectionId, @Param("desired") int desired);

    /**
     * 생성 시: [start..end] +1 (신규 행은 아직 없으니 excludeId 불필요)
     */
    @Modifying(flushAutomatically = true, clearAutomatically = false)
    @Query(value = """
            UPDATE tb_home_reco_item
               SET home_reco_item_slot_no = home_reco_item_slot_no + 1
             WHERE home_reco_section_id = :sectionId
               AND home_reco_item_slot_no BETWEEN :start AND :end
             ORDER BY home_reco_item_slot_no DESC
            """, nativeQuery = true)
    int shiftRangeRightByOne(@Param("sectionId") Long sectionId,
                             @Param("start") int start,
                             @Param("end") int end);
}
