package com.boot.loiteMsBack.user.repository;

import com.boot.loiteMsBack.user.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
}
