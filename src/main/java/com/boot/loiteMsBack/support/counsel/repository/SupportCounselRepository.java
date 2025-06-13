package com.boot.loiteMsBack.support.counsel.repository;

import com.boot.loiteMsBack.support.counsel.entity.SupportCounselEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SupportCounselRepository extends JpaRepository<SupportCounselEntity, Long> {

    Page<SupportCounselEntity> findByDeleteYn(String deleteYn, Pageable pageable);


    Optional<SupportCounselEntity> findByCounselIdAndDeleteYn(Long counselId, String deleteYn);

    @Query("""
            SELECT c FROM SupportCounselEntity c
            WHERE c.deleteYn = 'N'
              AND (
                  LOWER(c.counselTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
                  LOWER(c.counselContent) LIKE LOWER(CONCAT('%', :keyword, '%'))
              )
            """)
    Page<SupportCounselEntity> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("""
    SELECT c FROM SupportCounselEntity c
    WHERE c.deleteYn = 'N'
      AND c.counselReplyContent IS NULL
""")
    Page<SupportCounselEntity> findUnansweredWithoutKeyword(Pageable pageable);

    @Query("""
    SELECT c FROM SupportCounselEntity c
    WHERE c.deleteYn = 'N'
      AND c.counselReplyContent IS NULL
      AND (
        LOWER(c.counselTitle) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
        LOWER(c.counselContent) LIKE LOWER(CONCAT('%', :keyword, '%'))
      )
""")
    Page<SupportCounselEntity> findUnansweredWithKeyword(@Param("keyword") String keyword, Pageable pageable);

}
