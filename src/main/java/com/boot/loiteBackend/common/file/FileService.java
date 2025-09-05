package com.boot.loiteBackend.common.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    FileUploadResult save(MultipartFile file, String category);

    //  물리 경로의 파일 삭제 (성공 여부 반환, 예외 전파)
    boolean delete(String physicalPath);

    // 조용히 삭제 (예외 삼킴)
    void deleteQuietly(String physicalPath);
}
