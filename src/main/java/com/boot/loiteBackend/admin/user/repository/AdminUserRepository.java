package com.boot.loiteBackend.admin.user.repository;

import com.boot.loiteBackend.domain.user.general.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<UserEntity, Long> {
}
