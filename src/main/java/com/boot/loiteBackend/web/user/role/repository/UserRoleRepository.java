package com.boot.loiteBackend.web.user.role.repository;

import com.boot.loiteBackend.web.user.role.entity.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, String> {

    List<UserRoleEntity> findAllByActiveTrueOrderByDisplayOrderAsc();

    UserRoleEntity findByRoleCode(String roleCode);
}