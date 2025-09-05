package com.boot.loiteBackend.web.home.hero.repository;

import com.boot.loiteBackend.domain.home.hero.entity.HomeHeroEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface HomeHeroRepository extends JpaRepository<HomeHeroEntity, Long> {

    @Query("""
        select h
          from HomeHeroEntity h
         where upper(h.deletedYn) = 'N'
           and upper(h.displayYn) = 'Y'
           and (:now is null or h.startAt is null or h.startAt <= :now)
           and (:now is null or h.endAt   is null or h.endAt   >= :now)
        """)
    List<HomeHeroEntity> findActiveForNow(@Param("now") LocalDateTime now, Pageable pageable);
}
