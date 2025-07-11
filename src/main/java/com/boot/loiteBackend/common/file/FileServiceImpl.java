package com.boot.loiteBackend.common.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final FileStorageProperties fileProps;
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".gif", ".webp", ".pdf");

    public FileServiceImpl(FileStorageProperties fileProps) {
        this.fileProps = fileProps;
    }

    @Override
    public FileUploadResult save(MultipartFile file, String category) {
        try {
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException("파일이 비어있거나 존재하지 않습니다.");
            }

            String originalName = file.getOriginalFilename();
            if (originalName == null || !originalName.contains(".")) {
                throw new IllegalArgumentException("파일 확장자가 유효하지 않습니다.");
            }

            String extension = originalName.substring(originalName.lastIndexOf(".")).toLowerCase();
            if (!ALLOWED_EXTENSIONS.contains(extension)) {
                throw new IllegalArgumentException("지원하지 않는 파일 형식입니다: " + extension);
            }

            // 파일 이름 정리 및 경로 설정
            String cleanName = originalName.replaceAll("\\s+", "_");
            String fileName = UUID.randomUUID() + "_" + cleanName;

            String projectRoot = System.getProperty("user.dir");
            File uploadDir = new File(projectRoot, fileProps.getUploadDir() + "/" + category);

            if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                throw new IOException("업로드 디렉토리 생성 실패: " + uploadDir.getAbsolutePath());
            }

            File dest = new File(uploadDir, fileName);
            file.transferTo(dest);

            String urlPath = fileProps.getUploadUrlPrefix() + "/" + category + "/" + fileName;
            String physicalPath = dest.getAbsolutePath();

            return new FileUploadResult(urlPath, physicalPath);

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}

