package com.boot.loiteBackend.web.user.role.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRoleCodeEntity, String> {

    List<UserRoleCodeEntity> findAllByActiveTrueOrderByDisplayOrderAsc();

    UserRoleCodeEntity findByRoleCode(String roleCode);
}