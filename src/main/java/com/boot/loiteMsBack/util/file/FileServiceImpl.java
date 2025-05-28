package com.boot.loiteMsBack.util.file;

import com.boot.loiteMsBack.config.FileStorageProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final FileStorageProperties fileProps;

    public FileServiceImpl(FileStorageProperties fileProps) {
        this.fileProps = fileProps;
    }

    @Override
    public FileUploadResult save(MultipartFile file, String category) {
        try {
            // 원본 파일명 정리 (공백 -> 언더스코어)
            String originalName = file.getOriginalFilename();
            if (originalName == null) {
                throw new IllegalArgumentException("파일 이름이 없습니다.");
            }
            String cleanName = originalName.replaceAll("\\s+", "_");

            // 최종 파일명 생성
            String fileName = UUID.randomUUID() + "_" + cleanName;

            // 저장 경로 생성: 프로젝트 루트 기준 ./uploads/{category}
            String projectRoot = System.getProperty("user.dir");
            File uploadDir = new File(projectRoot, fileProps.getUploadDir() + "/" + category);

            // 폴더가 존재하지 않으면 생성
            if (!uploadDir.exists()) {
                boolean created = uploadDir.mkdirs();
                if (!created) {
                    throw new IOException("업로드 디렉토리 생성 실패: " + uploadDir.getAbsolutePath());
                }
            }

            // 실제 파일 저장
            File dest = new File(uploadDir, fileName);
            file.transferTo(dest);

            // 접근 가능한 URL 경로 반환: 예) /uploads/product/파일명
            String urlPath = fileProps.getUploadUrlPrefix() + "/" + category + "/" + fileName;
            String physicalPath = dest.getAbsolutePath();

            return new FileUploadResult(urlPath, physicalPath);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }
}
