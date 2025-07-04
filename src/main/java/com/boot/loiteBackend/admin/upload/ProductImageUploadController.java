package com.boot.loiteBackend.admin.upload;

import com.boot.loiteBackend.global.util.file.FileService;
import com.boot.loiteBackend.global.util.file.FileUploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin/upload")
public class ProductImageUploadController {

    private final FileService fileService;

    @PostMapping("/image")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("image") MultipartFile image) {
        if (image.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "파일 없음"));
        }

        FileUploadResult result = fileService.save(image, "product");

        if (result == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "저장 실패"));
        }

        return ResponseEntity.ok(Map.of("url", result.getUrlPath()));
    }
}
