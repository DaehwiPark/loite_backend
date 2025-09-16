package com.boot.loiteBackend.admin.manager.notice.service;

import com.boot.loiteBackend.admin.manager.notice.dto.AdminManagerNoticeCreateRequest;
import com.boot.loiteBackend.domain.manager.notice.entity.AdminManagerNoticeAttachment;
import com.boot.loiteBackend.domain.manager.notice.entity.AdminManagerNoticeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminManagerNoticeService {

    AdminManagerNoticeEntity createDraft(Long adminId, AdminManagerNoticeCreateRequest req);
    AdminManagerNoticeEntity publish(Long noticeId, Long adminId);
    Page<AdminManagerNoticeEntity> adminList(Pageable pageable);
    void softDelete(Long noticeId);
    Page<AdminManagerNoticeEntity> listVisible(Pageable pageable);
    long countUnread(Long managerId);
    void markRead(Long noticeId, Long managerId);
    void markAllRead(Long managerId);
    AdminManagerNoticeEntity getVisibleOrThrow(Long id);
    List<AdminManagerNoticeAttachment> getActiveAttachments(Long noticeId);
}
