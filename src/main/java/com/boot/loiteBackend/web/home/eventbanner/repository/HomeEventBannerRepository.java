package com.boot.loiteBackend.web.home.eventbanner.repository;

import com.boot.loiteBackend.domain.home.eventbanner.entity.HomeEventBannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface HomeEventBannerRepository extends JpaRepository<HomeEventBannerEntity, Long> {

    @Query(value = """
        WITH candidates AS (
            SELECT b.*, 1 AS pri
              FROM tb_home_event_banner b
             WHERE b.display_yn = 'Y'
               AND b.sort_order IN (1,2)
               AND (b.start_at IS NULL OR b.start_at <= :now)
               AND (b.end_at   IS NULL OR :now <= b.end_at)

            UNION ALL

            SELECT b.*, 2 AS pri
              FROM tb_home_event_banner b
             WHERE b.display_yn = 'Y'
               AND b.default_yn = 'Y'
               AND b.sort_order IN (1,2)
        )
        SELECT *
          FROM (
            SELECT c.*,
                   ROW_NUMBER() OVER (
                       PARTITION BY c.sort_order
                       ORDER BY c.pri, c.updated_at DESC, c.home_event_banner_id DESC
                   ) AS rn
              FROM candidates c
          ) x
         WHERE x.rn = 1
         ORDER BY x.sort_order
        """,
            nativeQuery = true)
    List<HomeEventBannerEntity> findCurrentTwoSlots(@Param("now") LocalDateTime now);
}