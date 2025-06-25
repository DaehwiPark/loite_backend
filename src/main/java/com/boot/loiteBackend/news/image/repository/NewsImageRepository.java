package com.boot.loiteBackend.news.image.repository;

import com.boot.loiteBackend.news.image.entity.NewsImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface NewsImageRepository extends JpaRepository<NewsImageEntity, Long> {

}
