package com.boot.loiteBackend.admin.manager.notice.controller;

import com.boot.loiteBackend.admin.manager.notice.dto.AdminManagerNoticeCreateRequest;
import com.boot.loiteBackend.admin.manager.notice.dto.AdminManagerNoticeResponse;
import com.boot.loiteBackend.admin.manager.notice.dto.AdminManagerNoticeUpdateRequest;
import com.boot.loiteBackend.admin.manager.notice.service.AdminManagerNoticeService;
import com.boot.loiteBackend.config.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/notices")
@RequiredArgsConstructor
@Tag(
        name = "관리자 공지 관리 API",
        description = "공지 초안 생성/발행/목록/소프트삭제 등 ADMIN 전용 엔드포인트"
//  ,securityRequirements = {@SecurityRequirement(name = "bearerAuth")}
)
public class AdminManagerNoticeController {

    private final AdminManagerNoticeService service;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "공지 초안 생성",
            description = """
            ADMIN이 공지 초안을 생성합니다. 기본적으로 status=DRAFT로 저장되며,
            중요도(importance), 상단고정(pinned), 만료시각(expiresAt) 등을 함께 설정할 수 있습니다.
            반환 값은 생성된 공지와 현재 첨부(없으면 빈 배열)입니다.
            """
    )
    /** 1) 기존 JSON 전용: 첨부 없음 */
    public AdminManagerNoticeResponse createDraftJson(
            @RequestBody AdminManagerNoticeCreateRequest payload,
            @AuthenticationPrincipal CustomUserDetails me
    ) {
        return service.createWithAttachments(me.getUserId(), payload, java.util.List.of(), false);
    }

    /** 2) 멀티파트: 첨부 O/X 모두 가능 + 선택 발행 */
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasRole('ADMIN')")
    public AdminManagerNoticeResponse createWithAttachmentsMultipart(
            @RequestPart("payload") AdminManagerNoticeCreateRequest payload,
            @RequestPart(name = "file", required = false) java.util.List<org.springframework.web.multipart.MultipartFile> files,
            @RequestParam(name = "publish", defaultValue = "false") boolean publish,
            @AuthenticationPrincipal CustomUserDetails me
    ) {
        return service.createWithAttachments(
                me.getUserId(),
                payload,
                (files == null ? java.util.List.of() : files),
                publish
        );
    }


    @PostMapping("/{id}/publish")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "공지 발행",
            description = """
            지정한 공지를 발행 상태(PUBLISHED)로 전환하고 publishedAt을 현재 시각으로 설정합니다.
            이미 PUBLISHED인 경우의 처리(무시/오류)는 서비스 정책에 따릅니다.
            """
    )
    public AdminManagerNoticeResponse publish(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails me
    ) {
        return service.publish(id, me.getUserId());  // 깔끔
    }

    @GetMapping("/{id}/detail")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(
            summary = "공지 상세 조회",
            description = "공지 상세 내용과 첨부 정보를 반환합니다. 반환 성공 후 클라이언트에서 `/read` 호출로 읽음 처리하는 흐름을 권장합니다."
    )
    public AdminManagerNoticeResponse detail(@PathVariable Long id) {
        return service.getDetail(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(
            summary = "공지 목록(ADMIN)",
            description = """
            ADMIN용 공지 목록을 페이지로 반환합니다. 상태/기간 필터는 서비스 레이어 정책에 따르며,
            관리화면에서 전체 상태(DRAFT/PUBLISHED/ARCHIVED) 확인 용도로 사용할 수 있습니다.
            """
    )
    public Page<AdminManagerNoticeResponse> list(
            @Parameter(description = "0부터 시작하는 페이지 인덱스", example = "0")
            @RequestParam(defaultValue="0") int page,
            @Parameter(description = "페이지 크기", example = "10")
            @RequestParam(defaultValue="10") int size
    ) {
        var p = service.adminList(PageRequest.of(page, size));
        return p.map(n -> AdminManagerNoticeResponse.of(n, service.getActiveAttachments(n.getId())));
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "공지 소프트 삭제",
            description = """
            공지의 deleted_at을 설정하여 논리 삭제합니다. 실제 데이터/첨부 파일은 물리 삭제하지 않으며,
            이후 목록/상세/뱃지 집계에서 제외됩니다.
            """
    )
    public void softDelete(
            @PathVariable Long id
            // 소유자 검증이 필요하다면 @AuthenticationPrincipal CustomUserDetails me 주입해서 서비스에 전달해도 됨
    ) {
        service.softDelete(id);
    }

    /** 공지 수정(초안/게시중 구분 없이 내용 갱신) */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminManagerNoticeResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody AdminManagerNoticeUpdateRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }
}
