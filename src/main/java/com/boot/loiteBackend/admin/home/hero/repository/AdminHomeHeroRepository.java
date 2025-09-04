package com.boot.loiteBackend.admin.home.hero.repository;

import com.boot.loiteBackend.domain.home.hero.entity.HomeHeroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AdminHomeHeroRepository extends JpaRepository<HomeHeroEntity, Long>, JpaSpecificationExecutor<HomeHeroEntity> {
}
