package com.boot.loiteBackend.admin.manager.notice.controller;

import com.boot.loiteBackend.admin.manager.notice.dto.AdminManagerNoticeResponse;
import com.boot.loiteBackend.admin.manager.notice.dto.AdminManagerUnreadItem;
import com.boot.loiteBackend.admin.manager.notice.service.AdminManagerNoticeService;
import com.boot.loiteBackend.config.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/manager-notices")
@RequiredArgsConstructor
@Tag(
        name = "매니저용 공지 열람/읽음 API",
        description = "ADMIN이 발행한 공지 목록/상세 조회 및 읽음 처리(뱃지 해제) 엔드포인트"
//  ,securityRequirements = {@SecurityRequirement(name = "bearerAuth")}
)
public class AdminManagerNoticeAlertController {

    private final AdminManagerNoticeService service;

    // 최신 공지 N건 (매니저 전용, 알림 벨 전용 가벼운 응답)
    @GetMapping("/latest")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "최신 공지 N건(매니저 알림용)")
    public List<AdminManagerNoticeResponse> latest(
            @RequestParam(defaultValue = "5") int size
    ) {
        int pageSize = Math.min(Math.max(size, 1), 20); // 1~20 방어
        var page = service.listVisible(PageRequest.of(0, pageSize));
        // 알림에서는 첨부 불필요 → 빈 리스트로 변환해 부하 줄임
        return page.getContent().stream()
                .map(n -> AdminManagerNoticeResponse.of(n, java.util.List.of()))
                .toList();
    }

    @PostMapping("/{id}/read")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(
            summary = "공지 읽음 처리",
            description = """
            공지 상세 진입 시 호출하여 읽음 상태를 기록합니다.
            이미 읽음인 경우에도 안전하게 처리됩니다(UPSERT/중복 방지).
            """
    )
    public void markRead(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails me) {
        service.markRead(id, me.getUserId());
    }

    @PostMapping("/read-all")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(
            summary = "공지 모두 읽음 처리",
            description = "현재 시점에 노출 가능한 모든 공지에 대해 읽음 상태를 일괄 기록합니다(뱃지 0으로)."
    )
    public void markAllRead(@AuthenticationPrincipal CustomUserDetails me) {
        service.markAllRead(me.getUserId());
    }

    @GetMapping("/unread")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "읽지 않은 공지 목록", description = "해당 매니저가 아직 읽지 않은 공지들만 페이지로 반환합니다.")
    public Page<AdminManagerNoticeResponse> unread(
            @AuthenticationPrincipal CustomUserDetails me,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        var p = service.unreadPage(me.getUserId(), PageRequest.of(page, size));
        return p.map(n -> AdminManagerNoticeResponse.of(n, service.getActiveAttachments(n.getId())));
    }

    @GetMapping("/unreadLight")
    @PreAuthorize("hasRole('MANAGER')")
    public Page<AdminManagerUnreadItem> unreadLight(
            @AuthenticationPrincipal CustomUserDetails me,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        return service.unreadPageLight(me.getUserId(), PageRequest.of(page, size));
    }

    @GetMapping("/unread-count")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(
            summary = "미확인 공지 수",
            description = "로그인한 매니저 기준으로 아직 읽지 않은(PUBLISHED & 유효) 공지의 개수를 반환합니다. 헤더 벨 아이콘 뱃지에 사용하세요."
    )
    public long unreadCount(@AuthenticationPrincipal CustomUserDetails me) {
        return service.countUnread(me.getUserId());
    }
}
