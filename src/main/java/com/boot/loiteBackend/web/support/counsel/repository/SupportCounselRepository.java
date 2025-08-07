package com.boot.loiteBackend.web.support.counsel.repository;

import com.boot.loiteBackend.domain.support.counsel.entity.SupportCounselEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportCounselRepository extends JpaRepository<SupportCounselEntity, Long> {
    List<SupportCounselEntity> findAllByCounselUserIdAndDeleteYn(Long userId, String deleteYn);
}