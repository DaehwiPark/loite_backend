package com.boot.loiteBackend.admin.support.counsel.repository;

import com.boot.loiteBackend.admin.support.counsel.entity.AdminSupportCounselEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AdminSupportCounselRepository extends JpaRepository<AdminSupportCounselEntity, Long>, JpaSpecificationExecutor<AdminSupportCounselEntity> {

    Optional<AdminSupportCounselEntity> findByCounselIdAndDeleteYn(Long counselId, String deleteYn);

}
