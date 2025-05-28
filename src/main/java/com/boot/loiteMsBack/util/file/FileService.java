package com.boot.loiteMsBack.util.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileUploadResult save(MultipartFile file, String category);
}
