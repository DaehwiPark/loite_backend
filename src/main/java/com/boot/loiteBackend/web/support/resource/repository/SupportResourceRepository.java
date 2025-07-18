package com.boot.loiteBackend.web.support.resource.repository;

import com.boot.loiteBackend.web.support.resource.entity.SupportResourceEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SupportResourceRepository extends JpaRepository<SupportResourceEntity, Long> {

    // 제품명 또는 모델명으로 OR 조건 검색
    Page<SupportResourceEntity> findByProduct_ProductNameContainingIgnoreCaseOrProduct_ProductModelNameContainingIgnoreCase(
            String productName, String productModelName, Pageable pageable
    );
}
