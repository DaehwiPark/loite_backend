package com.boot.loiteBackend.product.tag.repository;

import com.boot.loiteBackend.product.tag.entity.AdminTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminTagRepository extends JpaRepository<AdminTagEntity, Long> {
}
