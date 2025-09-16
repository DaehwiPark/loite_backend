package com.boot.loiteBackend.admin.manager.notice.repository;

import com.boot.loiteBackend.domain.manager.notice.entity.AdminManagerNoticeRead;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface AdminManagerNoticeReadRepository extends JpaRepository<AdminManagerNoticeRead, Long> {

    boolean existsByNoticeIdAndManagerId(Long noticeId, Long managerId);

    // 삭제된 행 수를 반환하는 파생 메서드 (주석/어노테이션 필요 없음)
    long deleteByNoticeId(Long noticeId);
}