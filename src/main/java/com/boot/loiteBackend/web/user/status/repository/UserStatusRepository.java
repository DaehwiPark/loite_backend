package com.boot.loiteBackend.web.user.status.repository;

import com.boot.loiteBackend.global.code.entity.UserStatusCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserStatusCodeRepository extends JpaRepository<UserStatusCodeEntity, String> {

    List<UserStatusCodeEntity> findAllByActiveTrueOrderByDisplayOrderAsc();

    UserStatusCodeEntity findByStatusCode(String statusCode);
}