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
            -- 1순위: 현재 시간에 유효한 배너
            SELECT b.*, 1 AS pri
              FROM tb_home_event_banner b
             WHERE b.display_yn = 'Y'
               AND b.default_slot IN (1,2)
               AND (b.start_at IS NULL OR b.start_at <= :now)
               AND (b.end_at   IS NULL OR :now <= b.end_at)
            UNION ALL
            -- 2순위: 기본 배너(default_yn='Y')
            SELECT b.*, 2 AS pri
              FROM tb_home_event_banner b
             WHERE b.display_yn = 'Y'
               AND b.default_yn = 'Y'
               AND b.default_slot IN (1,2)
        )
        SELECT *
          FROM (
            SELECT c.*,
                   ROW_NUMBER() OVER (
                       PARTITION BY c.default_slot
                       ORDER BY c.pri, c.updated_at DESC, c.home_event_banner_id DESC
                   ) AS rn
              FROM candidates c
          ) x
         WHERE x.rn = 1
         ORDER BY x.default_slot
        """,
            nativeQuery = true)
    List<HomeEventBannerEntity> findCurrentTwoSlots(@Param("now") LocalDateTime now);
}
