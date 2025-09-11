package com.boot.loiteBackend.common.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileUploadResult {
    private final String urlPath;       // 웹 접근 경로 (/uploads/etc/xxx.jpg)
    private final String physicalPath;  // 실제 서버 경로 (예: C:/workspace/uploads/etc/xxx.jpg)
    private final String fileName;      // 저장된 파일명 (UUID_원본명)
    private final long size;            // 파일 크기 (byte)
    private final String contentType;   // MIME 타입 (예: image/jpeg, video/mp4)
}