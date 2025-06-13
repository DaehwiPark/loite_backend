package com.boot.loiteMsBack.news.image.repository;

import com.boot.loiteMsBack.news.image.entity.NewsImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface NewsImageRepository extends JpaRepository<NewsImageEntity, Long> {

}
