package com.boot.loiteBackend.web.home.recommend.section.repository;

import com.boot.loiteBackend.domain.home.recommend.section.entity.HomeRecoSectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HomeRecoSectionRepository extends JpaRepository<HomeRecoSectionEntity, Long> {
}