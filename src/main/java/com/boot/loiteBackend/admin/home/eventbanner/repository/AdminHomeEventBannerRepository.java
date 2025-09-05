package com.boot.loiteBackend.admin.home.eventbanner.repository;

import com.boot.loiteBackend.domain.home.eventbanner.entity.HomeEventBannerEntity;
import org.springframework.data.jpa.repository.*;

public interface AdminHomeEventBannerRepository extends JpaRepository<HomeEventBannerEntity, Long>, JpaSpecificationExecutor<HomeEventBannerEntity> {

}