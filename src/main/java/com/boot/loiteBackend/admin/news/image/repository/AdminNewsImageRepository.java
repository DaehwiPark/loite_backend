package com.boot.loiteBackend.admin.news.image.repository;

import com.boot.loiteBackend.admin.news.image.entity.AdminNewsImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface AdminNewsImageRepository extends JpaRepository<AdminNewsImageEntity, Long> {

}
