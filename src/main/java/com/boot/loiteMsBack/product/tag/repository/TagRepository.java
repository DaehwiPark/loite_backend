package com.boot.loiteMsBack.product.tag.repository;

import com.boot.loiteMsBack.product.tag.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
}
