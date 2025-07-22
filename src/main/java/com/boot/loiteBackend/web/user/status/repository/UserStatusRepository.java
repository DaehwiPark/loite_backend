package com.boot.loiteBackend.web.user.status.repository;

import com.boot.loiteBackend.web.user.status.entity.UserStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatusEntity, String> {

}