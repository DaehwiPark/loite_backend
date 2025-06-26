package com.boot.loiteBackend.admin.user.repository;

import com.boot.loiteBackend.admin.user.Entity.AdminUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUserEntity, Long> {
}
