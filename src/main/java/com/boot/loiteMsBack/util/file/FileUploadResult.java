package com.boot.loiteMsBack.util.file;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileUploadResult {
    private final String urlPath;      // 웹 접근 경로 (/uploads/etc/xxx.jpg)
    private final String physicalPath; // 실제 경로 (C:/workspace/uploads/etc/xxx.jpg)
}
