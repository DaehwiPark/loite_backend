package com.boot.loiteBackend.admin.manager.notice.controller;

import com.boot.loiteBackend.admin.manager.notice.repository.AdminManagerNoticeAttachmentRepository;
import com.boot.loiteBackend.common.file.FileService;
import com.boot.loiteBackend.common.file.FileUploadResult;
import com.boot.loiteBackend.domain.manager.notice.entity.AdminManagerNoticeAttachment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/notices")
@RequiredArgsConstructor
@Tag(name = "관리자 공지사항 첨부파일", description = "관리자 공지사항 첨부파일")
public class AdminManagerAttachmentController {

    private final FileService fileService;
    private final AdminManagerNoticeAttachmentRepository attRepo;

    @PostMapping(
            value = "/{id}/attachments",
            consumes = "multipart/form-data"
    )
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Operation(summary = "공지사항 첨부파일 업로드(여러 개 가능)", description = "파트 이름은 file")
    public List<Long> upload(
            @PathVariable Long id,
            @RequestParam("file") List<MultipartFile> files // file 파트를 여러 번 보내면 List로 매핑됨
    ) throws Exception {
        if (files == null || files.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "file part is required");
        }

        // 정렬값 이어붙이고 싶으면 기존 max sortOrder 조회 (선택)
        int sortOrder = 0; // 필요 시 레포지토리에서 max 가져와서 시작값 조정

        List<Long> ids = new ArrayList<>();
        for (MultipartFile f : files) {
            if (f.isEmpty()) continue;

            FileUploadResult r = fileService.save(f, "notice/" + id); // 구현체 시그니처에 맞게

            var att = AdminManagerNoticeAttachment.builder()
                    .noticeId(id)
                    .fileUrl(r.getUrlPath())
                    .filePath(r.getPhysicalPath())
                    .originalName(f.getOriginalFilename())
                    .mimeType(f.getContentType())
                    .fileSizeBytes(f.getSize())
                    .sortOrder(sortOrder++)
                    .build();

            // createdAt NOT NULL 제약이 있으면 직접 세팅(엔티티에 @PrePersist 없을 때)
            try {
                // att.setCreatedAt(LocalDateTime.now()); // 엔티티에 세터가 있으면 주석 해제
            } catch (Exception ignore) {}

            attRepo.save(att);
            ids.add(att.getId());
        }
        return ids;
    }

    @GetMapping("/{noticeId}/attachments/{attId}/download")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    @Operation(summary = "공지 첨부 다운로드")
    public ResponseEntity<Resource> download(
            @PathVariable Long noticeId,
            @PathVariable Long attId
    ) throws java.io.IOException {

        var att = attRepo.findById(attId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "attachment not found"));
        if (!noticeId.equals(att.getNoticeId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "attachment not found for this notice");
        }
        // 실제 파일 경로 확인
        var pathStr = att.getFilePath(); // FileService.save()에서 저장한 물리 경로
        if (pathStr == null || pathStr.isBlank()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "file path missing");
        }
        java.nio.file.Path path = java.nio.file.Paths.get(pathStr);
        if (!java.nio.file.Files.exists(path)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "file not found");
        }

        String filename = (att.getOriginalName() != null && !att.getOriginalName().isBlank())
                ? att.getOriginalName()
                : path.getFileName().toString();

        var mediaType = org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
        try {
            if (att.getMimeType() != null && !att.getMimeType().isBlank()) {
                mediaType = org.springframework.http.MediaType.parseMediaType(att.getMimeType());
            } else {
                String probe = java.nio.file.Files.probeContentType(path);
                if (probe != null) mediaType = org.springframework.http.MediaType.parseMediaType(probe);
            }
        } catch (Exception ignore) {}

        var resource = new org.springframework.core.io.InputStreamResource(java.nio.file.Files.newInputStream(path));
        long size = java.nio.file.Files.size(path);

        return org.springframework.http.ResponseEntity.ok()
                .contentType(mediaType)
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        org.springframework.http.ContentDisposition.attachment()
                                .filename(filename, java.nio.charset.StandardCharsets.UTF_8)
                                .build().toString())
                .contentLength(size)
                .body(resource);
    }


}
