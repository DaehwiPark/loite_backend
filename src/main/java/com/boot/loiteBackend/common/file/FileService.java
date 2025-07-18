package com.boot.loiteBackend.common.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileUploadResult save(MultipartFile file, String category);
}
