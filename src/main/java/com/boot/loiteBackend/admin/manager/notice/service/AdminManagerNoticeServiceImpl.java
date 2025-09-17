package com.boot.loiteBackend.admin.manager.notice.service;

import com.boot.loiteBackend.admin.manager.notice.dto.AdminManagerNoticeCreateRequest;
import com.boot.loiteBackend.admin.manager.notice.dto.AdminManagerNoticeResponse;
import com.boot.loiteBackend.admin.manager.notice.dto.AdminManagerNoticeUpdateRequest;
import com.boot.loiteBackend.admin.manager.notice.repository.AdminManagerNoticeAttachmentRepository;
import com.boot.loiteBackend.admin.manager.notice.repository.AdminManagerNoticeReadRepository;
import com.boot.loiteBackend.admin.manager.notice.repository.AdminManagerNoticeRepository;
import com.boot.loiteBackend.domain.manager.notice.entity.AdminManagerNoticeAttachment;
import com.boot.loiteBackend.domain.manager.notice.entity.AdminManagerNoticeEntity;
import com.boot.loiteBackend.domain.manager.notice.entity.AdminManagerNoticeRead;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminManagerNoticeServiceImpl implements AdminManagerNoticeService {

    private final AdminManagerNoticeRepository noticeRepo;
    private final AdminManagerNoticeReadRepository readRepo;
    private final AdminManagerNoticeAttachmentRepository attRepo;

    /* ================== ADMIN ================== */

    /*
      ADMIN이 공지사항 초안을 생성한다.
      <p>
      - 기본 상태를 DRAFT로 저장한다.<br>
      - importance/pinned/expiresAt 등 메타를 함께 반영한다.<br>
      - 생성자는 createdByAdmin에 기록된다.

      @param adminId 작성하는 ADMIN의 식별자
      @param req     제목/본문/중요도/고정/만료시각 등을 담은 요청 DTO
      @return 저장된 공지 엔티티(초안)
     */
    @Override
    @Transactional
    public AdminManagerNoticeEntity createDraft(Long adminId, AdminManagerNoticeCreateRequest req) {
        var n = AdminManagerNoticeEntity.builder()
                .title(req.getTitle())
                .contentMd(req.getContentMd())
                .importance(req.getImportance() == null ? 0 : req.getImportance())
                .pinned(req.getPinned() != null && req.getPinned())
                .status(AdminManagerNoticeEntity.NoticeStatus.DRAFT)
                .expiresAt(req.getExpiresAt())
                .createdByAdmin(adminId)
                .build();
        return noticeRepo.save(n);
    }

    /*
      지정된 공지를 발행(PUBLISHED) 상태로 전환한다.
      <p>
      - publishedAt을 현재 시각으로 설정한다.<br>
      - 수정자(updatedByAdmin)를 기록한다.<br>
      - 이미 PUBLISHED인 케이스의 정책(그대로 유지/예외)은 서비스 정책에 따른다.

      @param noticeId 발행할 공지 ID
      @param adminId  발행을 수행하는 ADMIN ID
      @return 발행된 공지 엔티티
     */
    @Override
    @Transactional
    public AdminManagerNoticeEntity publish(Long noticeId, Long adminId) {
        var n = noticeRepo.findById(noticeId).orElseThrow();
        n.setStatus(AdminManagerNoticeEntity.NoticeStatus.PUBLISHED);
        n.setPublishedAt(LocalDateTime.now());
        n.setUpdatedByAdmin(adminId);
        return n;
    }

    /*
      ADMIN 화면용 공지 목록을 페이지로 조회한다.
      <p>
      - 상태/기간 무관하게 전체 목록을 반환한다(Repository 정책에 따름).<br>
      - 운영 화면에서 DRAFT/PUBLISHED/ARCHIVED를 모두 확인할 때 사용한다.

      @param pageable 페이지/정렬 정보
      @return 공지 페이지
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdminManagerNoticeEntity> adminList(Pageable pageable) {
        return noticeRepo.findAdminListAlive(pageable);
    }

    /*
      공지를 소프트 삭제한다.
      <p>
      - deletedAt을 현재 시각으로 설정한다.<br>
      - 첨부는 소프트 삭제 모델이므로 별도 물리 삭제를 수행하지 않는다(조회 시 제외).

      @param noticeId 소프트 삭제할 공지 ID
     */
    @Override
    @Transactional
    public void softDelete(Long noticeId) {
        var n = noticeRepo.findById(noticeId).orElseThrow();
        n.setDeletedAt(LocalDateTime.now());
        // NOTE: 첨부는 deletedAt 필터로 제외. 필요 시 첨부에도 동일 타임스탬프를 마킹하는 로직을 추가 가능.
    }

    /* ================ MANAGER ================== */

    /*
      MANAGER에게 현재 노출 가능한(PUBLISHED & 삭제X & 만료X) 공지 목록을 페이지로 조회한다.

      @param pageable 페이지/정렬 정보
      @return 노출 가능한 공지 페이지
     */
    @Override
    @Transactional(readOnly = true)
    public Page<AdminManagerNoticeEntity> listVisible(Pageable pageable) {
        return noticeRepo.findVisible(
                pageable,
                LocalDateTime.now(),
                AdminManagerNoticeEntity.NoticeStatus.PUBLISHED
        );
    }

    /*
      특정 MANAGER 기준으로 아직 읽지 않은 공지의 개수를 반환한다.
      <p>
      - 정의: notice_read에 (noticeId, managerId) 레코드가 없는 공지의 수.<br>
      - 헤더 벨 아이콘 뱃지 표시에 활용한다.

      @param managerId MANAGER ID
      @return 미확인(미읽음) 공지 개수
     */
    @Override
    @Transactional(readOnly = true)
    public long countUnread(Long managerId) {
        return noticeRepo.countUnread(
                managerId,
                LocalDateTime.now(),
                AdminManagerNoticeEntity.NoticeStatus.PUBLISHED
        );
    }

    /*
      공지 한 건을 읽음 처리한다.
      <p>
      - 이미 읽음이면 아무 작업도 하지 않는다(중복 방지).<br>
      - race condition 회피가 중요하면 DB의 UNIQUE 제약에 의존한 UPSERT(try-catch)로 변경 가능.

      @param noticeId  공지 ID
      @param managerId MANAGER ID
     */
    @Override
    @Transactional
    public void markRead(Long noticeId, Long managerId) {
        if (readRepo.existsByNoticeIdAndManagerId(noticeId, managerId)) return;

        var r = AdminManagerNoticeRead.builder()
                .noticeId(noticeId)
                .managerId(managerId)
                .readAt(LocalDateTime.now())
                .build();

        // NOTE: 동시성에 아주 민감하면 아래처럼 UNIQUE 충돌을 허용하고 무시해도 됨.
        // try {
        //     readRepo.save(r);
        // } catch (DataIntegrityViolationException ignore) { /* already read */ }
        readRepo.save(r);
    }

    /*
      현재 시점에 노출 가능한 모든 공지를 일괄 읽음 처리한다.
      <p>
      - 기본 구현은 페이지를 순회하며 markRead를 호출한다(간단하지만 데이터가 아주 많으면 비효율).<br>
      - 대량 데이터 환경에서는 네이티브 INSERT ... SELECT(UPSERT) 방식으로 교체 권장.

      @param managerId MANAGER ID
     */
    @Override
    @Transactional
    public void markAllRead(Long managerId) {
        // 성능형: 네이티브로 INSERT IGNORE ... SELECT 또는 ON DUPLICATE KEY UPDATE 권장.
        Page<AdminManagerNoticeEntity> page = listVisible(PageRequest.of(0, 200));
        while (!page.isEmpty()) {
            for (var n : page.getContent()) {
                markRead(n.getId(), managerId);
            }
            if (!page.hasNext()) break;
            page = listVisible(page.nextPageable());
        }
    }

    /*
      공지 상세를 조회하되, 현재 노출 가능한 공지만 허용한다.
      <p>
      - 존재하지 않거나, PUBLISHED가 아니거나, 삭제/만료된 경우 예외를 던진다.<br>
      - 컨트롤러에서 적절한 상태코드(404/400 등)로 매핑 필요.

      @param id 공지 ID
      @return 노출 가능한 공지 엔티티
      @throws IllegalStateException 공지가 노출 불가인 경우
     */
    @Override
    @Transactional(readOnly = true)
    public AdminManagerNoticeEntity getVisibleOrThrow(Long id) {
        var n = noticeRepo.findById(id).orElseThrow();
        if (!n.isVisibleNow()) throw new IllegalStateException("Notice not visible");
        return n;
    }

    /*
      공지에 연결된 '유효한(소프트삭제되지 않은)' 첨부 목록을 정렬 순서대로 조회한다.

      @param noticeId 공지 ID
      @return 첨부 엔티티 목록(삭제되지 않은 것만, sortOrder asc, id asc)
     */
    @Override
    @Transactional(readOnly = true)
    public List<AdminManagerNoticeAttachment> getActiveAttachments(Long noticeId) {
        return attRepo.findByNoticeIdAndDeletedAtIsNullOrderBySortOrderAscIdAsc(noticeId);
    }

    @Override
    public AdminManagerNoticeResponse update(Long id, AdminManagerNoticeUpdateRequest req) {
        AdminManagerNoticeEntity n = noticeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notice not found"));

        n.setTitle(req.title());
        n.setContentMd(req.contentMd());
        n.setImportance(req.importance());      // 0/1
        n.setPinned(req.pinned());
        n.setExpiresAt(req.expiresAt());        // null이면 만료 해제

        // 필요하면 수정자 기록
        // n.setUpdatedByAdmin(currentAdminId);

        // 변경감지로 저장되고, 응답 DTO로 변환
        return toResponse(n);
    }

    @Override
    public AdminManagerNoticeResponse publish(Long id) {
        AdminManagerNoticeEntity n = noticeRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notice not found"));

        if (n.getStatus() != AdminManagerNoticeEntity.NoticeStatus.PUBLISHED) {
            n.setStatus(AdminManagerNoticeEntity.NoticeStatus.PUBLISHED);
            if (n.getPublishedAt() == null) {
                n.setPublishedAt(LocalDateTime.now());
            }
        }
        // 재발행 시 publishedAt을 ‘항상’ 갱신하고 싶다면 위 if 대신 아래 한 줄로:
        // n.setPublishedAt(LocalDateTime.now());

        return toResponse(n);
    }

    /** 엔티티 -> 응답 DTO */
    private AdminManagerNoticeResponse toResponse(AdminManagerNoticeEntity n) {
        // 빈 리스트 보장되므로 null 체크 불필요
        var atts = attRepo.findByNoticeIdAndDeletedAtIsNullOrderBySortOrderAscIdAsc(n.getId());
        return AdminManagerNoticeResponse.of(n, atts);
    }
}
