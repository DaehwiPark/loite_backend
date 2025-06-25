package com.boot.loiteBackend.admin.news.general.controller;

import com.boot.loiteBackend.admin.news.general.dto.AdminNewsDto;
import com.boot.loiteBackend.admin.news.general.service.AdminNewsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/news")
@Tag(name = "새소식 API", description = "새소식(News) 등록, 조회, 수정, 삭제 기능을 제공합니다.")
public class AdminNewsController {

    private final AdminNewsService adminNewsService;

    @Operation(summary = "새소식 등록", description = "새로운 새소식(뉴스)을 등록합니다. 썸네일 파일과 본문 내용을 포함합니다.")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AdminNewsDto> createNews(
            @Parameter(description = "뉴스 정보 JSON", required = true, schema = @Schema(implementation = AdminNewsDto.class))
            @RequestPart("news") AdminNewsDto adminNewsDto,
            @Parameter(description = "썸네일 이미지 파일", required = true)
            @RequestPart("thumbnail") MultipartFile thumbnail
    ) {
        AdminNewsDto created = adminNewsService.createNews(adminNewsDto, thumbnail);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "새소식 수정", description = "기존 새소식의 내용을 수정합니다. 뉴스 ID를 path로 전달하고, 수정할 내용을 body에 포함시켜야 합니다.")
    @PutMapping("/{id}")
    public ResponseEntity<AdminNewsDto> updateNews(@PathVariable Long id, @RequestBody AdminNewsDto adminNewsDto) {
        AdminNewsDto updated = adminNewsService.updateNews(id, adminNewsDto);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "새소식 삭제", description = "특정 새소식(뉴스)를 삭제합니다. 해당 뉴스 ID를 path로 전달합니다. 관련된 이미지도 함께 삭제됩니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNews(@PathVariable Long id) {
        adminNewsService.deleteNews(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "새소식 전체 조회", description = "등록된 모든 새소식(뉴스)을 리스트로 조회합니다.")
    @GetMapping
    public ResponseEntity<List<AdminNewsDto>> getAllNews() {
        List<AdminNewsDto> list = adminNewsService.getAllNews();
        return ResponseEntity.ok(list);
    }

    @Operation(summary = "새소식 단건 조회", description = "특정 뉴스 ID로 새소식을 조회합니다. 본문과 관련 이미지 URL도 함께 반환될 수 있습니다.")
    @GetMapping("/{id}")
    public ResponseEntity<AdminNewsDto> getNewsById(@PathVariable Long id) {
        AdminNewsDto news = adminNewsService.getNewsById(id);
        return ResponseEntity.ok(news);
    }
}
