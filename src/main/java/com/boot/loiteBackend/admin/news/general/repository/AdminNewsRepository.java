package com.boot.loiteBackend.admin.news.general.repository;

import com.boot.loiteBackend.admin.news.general.entity.AdminNewsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminNewsRepository extends JpaRepository<AdminNewsEntity, Long> {
}
