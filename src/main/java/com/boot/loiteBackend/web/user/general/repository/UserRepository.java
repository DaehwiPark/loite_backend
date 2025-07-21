package com.boot.loiteBackend.web.user.general.repository;

import com.boot.loiteBackend.web.user.general.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    boolean existsByUserEmail(String userEmail);

    Optional<UserEntity> findByUserEmail(String userEmail);
}
