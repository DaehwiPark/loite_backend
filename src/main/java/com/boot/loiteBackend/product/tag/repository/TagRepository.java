package com.boot.loiteBackend.product.tag.repository;

import com.boot.loiteBackend.product.tag.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
}
