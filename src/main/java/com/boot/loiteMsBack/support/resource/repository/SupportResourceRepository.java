package com.boot.loiteMsBack.support.resource.repository;

import com.boot.loiteMsBack.support.resource.entity.SupportResourceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SupportResourceRepository extends JpaRepository<SupportResourceEntity, Long> {

    @Query("SELECT r FROM SupportResourceEntity r " +
            "WHERE (:keyword IS NULL OR :keyword = '' " +
            "OR r.resourceProductName LIKE %:keyword% " +
            "OR r.resourceModelName LIKE %:keyword%)")
    Page<SupportResourceEntity> findByKeyword(
            @Param("keyword") String keyword,
            Pageable pageable
    );
}
