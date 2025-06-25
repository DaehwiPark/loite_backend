package com.boot.loiteBackend.web.user.repository;

import com.boot.loiteBackend.web.user.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
