package com.boot.loiteBackend.admin.support.notice.repository;

import com.boot.loiteBackend.domain.support.notice.entity.SupportNoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AdminSupportNoticeRepository extends JpaRepository<SupportNoticeEntity, Long> {
    @Query("SELECT n FROM SupportNoticeEntity n WHERE (:keyword IS NULL OR n.noticeTitle LIKE %:keyword%)")
    Page<SupportNoticeEntity> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
