package com.boot.loiteMsBack.support.counsel.repository;

import com.boot.loiteMsBack.support.counsel.entity.SupportCounselEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SupportCounselRepository extends JpaRepository<SupportCounselEntity, Long> {
    List<SupportCounselEntity> findByDelYn(String delYn);
    List<SupportCounselEntity> findByDelYnAndCounselReplyContentIsNull(String delYn);
    Optional<SupportCounselEntity> findByCounselIdAndDelYn(Long counselId, String delYn);
}