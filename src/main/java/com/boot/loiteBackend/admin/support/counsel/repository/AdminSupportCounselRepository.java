package com.boot.loiteBackend.admin.support.counsel.repository;

import com.boot.loiteBackend.domain.support.counsel.entity.SupportCounselEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface AdminSupportCounselRepository extends JpaRepository<SupportCounselEntity, Long>, JpaSpecificationExecutor<SupportCounselEntity> {

    Optional<SupportCounselEntity> findByCounselIdAndDeleteYn(Long counselId, String deleteYn);

}
