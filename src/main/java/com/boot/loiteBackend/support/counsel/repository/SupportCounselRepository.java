package com.boot.loiteBackend.support.counsel.repository;

import com.boot.loiteBackend.support.counsel.entity.SupportCounselEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SupportCounselRepository extends JpaRepository<SupportCounselEntity, Long>, JpaSpecificationExecutor<SupportCounselEntity> {

    Optional<SupportCounselEntity> findByCounselIdAndDeleteYn(Long counselId, String deleteYn);

}
