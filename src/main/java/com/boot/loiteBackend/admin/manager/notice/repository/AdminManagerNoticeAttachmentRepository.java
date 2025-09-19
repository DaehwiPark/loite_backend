package com.boot.loiteBackend.admin.manager.notice.repository;

import com.boot.loiteBackend.domain.manager.notice.entity.AdminManagerNoticeAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AdminManagerNoticeAttachmentRepository extends JpaRepository<AdminManagerNoticeAttachment, Long> {
    List<AdminManagerNoticeAttachment> findByNoticeIdAndDeletedAtIsNullOrderBySortOrderAscIdAsc(Long noticeId);

    Optional<AdminManagerNoticeAttachment> findByIdAndNoticeIdAndDeletedAtIsNull(Long id, Long noticeId);

}
