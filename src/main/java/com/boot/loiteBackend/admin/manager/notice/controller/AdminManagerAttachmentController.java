package com.boot.loiteBackend.admin.manager.notice.controller;


import com.boot.loiteBackend.admin.manager.notice.repository.AdminManagerNoticeAttachmentRepository;
import com.boot.loiteBackend.common.file.FileService;
import com.boot.loiteBackend.common.file.FileUploadResult;
import com.boot.loiteBackend.domain.manager.notice.entity.AdminManagerNoticeAttachment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/notices")
@RequiredArgsConstructor
@Tag(name = "관리자 공지사항 첨부파일", description = "관리자 공지사항 첨부파일")
public class AdminManagerAttachmentController {

    private final FileService fileService;
    private final AdminManagerNoticeAttachmentRepository attRepo;

    @PostMapping("/{id}/attachments")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(
            summary = "공지사항 등록 시 첨부파일 등록",
            description = "공지사항 등록 시 첨부파일 등록"
    )
    public Long upload(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws Exception {
        // 저장 (예: 하위폴더 "notice/{id}/")
        FileUploadResult r = fileService.save(file, "notice/" + id); // 구현체에 맞게 시그니처 확인
        var att = AdminManagerNoticeAttachment.builder()
                .noticeId(id)
                .fileUrl(r.getUrlPath())
                .filePath(r.getPhysicalPath())
                .originalName(file.getOriginalFilename())
                .mimeType(file.getContentType())
                .fileSizeBytes(file.getSize())
                .sortOrder(0)
                .build();
        attRepo.save(att);
        return att.getId();
    }
}
