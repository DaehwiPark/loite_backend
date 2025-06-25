package com.boot.loiteBackend.user.repository;

import com.boot.loiteBackend.user.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
