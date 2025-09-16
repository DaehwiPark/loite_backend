package com.boot.loiteBackend.admin.manager.notice.repository;

import com.boot.loiteBackend.domain.manager.notice.entity.AdminManagerNoticeAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminManagerNoticeAttachmentRepository extends JpaRepository<AdminManagerNoticeAttachment, Long> {
    List<AdminManagerNoticeAttachment> findByNoticeIdAndDeletedAtIsNullOrderBySortOrderAscIdAsc(Long noticeId);
}
