package com.boot.loiteBackend.admin.manager.notice.controller;

import com.boot.loiteBackend.admin.manager.notice.dto.AdminManagerNoticeResponse;
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

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(
            summary = "공지 목록 조회(매니저)",
            description = """
            상태가 PUBLISHED이고 삭제되지 않았으며(expired 미도래) 현재 노출 가능한 공지들을 페이지로 반환합니다.
            정렬은 서비스 레이어 정책(예: pinned desc, publishedAt desc 등)에 따릅니다.
            """
    )
    public Page<AdminManagerNoticeResponse> list(
            @Parameter(description = "0부터 시작하는 페이지 인덱스", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "페이지 크기(기본 10)", example = "10")
            @RequestParam(defaultValue = "10") int size
    ) {
        var p = service.listVisible(PageRequest.of(page, size));
        return p.map(n -> AdminManagerNoticeResponse.of(n, service.getActiveAttachments(n.getId())));
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

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(
            summary = "공지 상세 조회",
            description = "공지 상세 내용과 첨부 정보를 반환합니다. 반환 성공 후 클라이언트에서 `/read` 호출로 읽음 처리하는 흐름을 권장합니다."
    )
    public AdminManagerNoticeResponse detail(@PathVariable Long id) {
        var n = service.getVisibleOrThrow(id);
        return AdminManagerNoticeResponse.of(n, service.getActiveAttachments(id));
    }
}
