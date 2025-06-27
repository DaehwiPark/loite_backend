package com.boot.loiteBackend.admin.product.tag.repository;

import com.boot.loiteBackend.admin.product.tag.entity.AdminTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminTagRepository extends JpaRepository<AdminTagEntity, Long> {
}
