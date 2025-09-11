package com.boot.loiteBackend.admin.home.magazinebanner.repository;

import com.boot.loiteBackend.domain.home.magazinebanner.entity.HomeMagazineBannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminHomeMagazineBannerRepository extends JpaRepository<HomeMagazineBannerEntity, Long> {
}