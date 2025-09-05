package com.boot.loiteBackend.web.home.topbar.repository;

import com.boot.loiteBackend.domain.home.topbar.entity.HomeTopBarEntity;
import org.springframework.data.jpa.repository.*;
import java.util.Optional;

public interface HomeTopBarWebRepository extends JpaRepository<HomeTopBarEntity, Long> {

    @Query("""
        select t
          from HomeTopBarEntity t
         where upper(t.defaultYn) = 'Y'
           and upper(t.displayYn) = 'Y'
        """)
    Optional<HomeTopBarEntity> findDefaultVisibleOne();
}
