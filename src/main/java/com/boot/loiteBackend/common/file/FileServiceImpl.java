package com.boot.loiteBackend.common.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final FileStorageProperties fileProps;

    // 허용 확장자 화이트리스트
    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of(".jpg", ".jpeg", ".png", ".gif", ".webp", ".pdf", ".svg", ".mp4");

    public FileServiceImpl(FileStorageProperties fileProps) {
        this.fileProps = fileProps;
    }

    @Override
    public FileUploadResult save(MultipartFile file, String category) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있거나 존재하지 않습니다.");
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || !originalName.contains(".")) {
            throw new IllegalArgumentException("파일 확장자가 유효하지 않습니다.");
        }

        // 확장자 검증
        String ext = extractExtension(originalName);
        if (!ALLOWED_EXTENSIONS.contains(ext)) {
            throw new IllegalArgumentException("지원하지 않는 파일 형식입니다: " + ext);
        }

        // 카테고리/파일명 정리
        String safeCategory = sanitizeCategory(category);
        String cleanName = sanitizeFilename(originalName);
        String fileName = UUID.randomUUID() + "_" + cleanName;

        try {
            // 업로드 루트 경로 계산 (프로젝트 루트 기준)
            String projectRoot = System.getProperty("user.dir");
            Path uploadDir = Paths.get(projectRoot, fileProps.getUploadDir(), safeCategory)
                    .toAbsolutePath().normalize();

            // 디렉토리 보장
            Files.createDirectories(uploadDir);

            // 저장 대상
            Path target = uploadDir.resolve(fileName).normalize();

            // 경로 탈출 방지
            if (!target.startsWith(uploadDir)) {
                throw new SecurityException("잘못된 파일 경로가 감지되었습니다.");
            }

            // 저장 (덮어쓰기 방지)
            if (Files.exists(target)) {
                throw new IOException("동일한 파일명이 이미 존재합니다: " + target);
            }

            // 실제 저장
            file.transferTo(target.toFile());

            // URL/물리경로 생성
            String urlPath = fileProps.getUploadUrlPrefix() + "/" + safeCategory + "/" + fileName;
            String physicalPath = target.toString();

            // 파일 메타데이터 추출
            long size = file.getSize();
            String contentType = file.getContentType();

            // FileUploadResult로 반환
            return new FileUploadResult(urlPath, physicalPath, fileName, size, contentType);

        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패", e);
        }
    }

    @Override
    public boolean delete(String physicalPath) {
        if (physicalPath == null || physicalPath.isBlank()) {
            return false;
        }
        try {
            return Files.deleteIfExists(Path.of(physicalPath));
        } catch (IOException e) {
            throw new RuntimeException("파일 삭제 실패: " + physicalPath, e);
        }
    }

    @Override
    public void deleteQuietly(String physicalPath) {
        if (physicalPath == null || physicalPath.isBlank()) return;
        try {
            Files.deleteIfExists(Path.of(physicalPath));
        } catch (Exception ignored) {
            // 필요 시 로깅
        }
    }

    /* ==========================
     * 내부 유틸
     * ========================== */

    private static String extractExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".")).toLowerCase();
    }

    private static String sanitizeFilename(String filename) {
        String name = filename.replaceAll("\\s+", "_");
        name = name.replaceAll("[\\\\/:*?\"<>|]", "");
        name = name.replaceAll("^\\.+", "").replaceAll("\\.+$", "");
        return name;
    }

    private static String sanitizeCategory(String category) {
        if (category == null || category.isBlank()) return "etc";
        String c = category.toLowerCase().replaceAll("[^a-z0-9_\\-/]", "");
        c = c.replaceAll("/+", "/");
        if (c.startsWith("/")) c = c.substring(1);
        if (c.endsWith("/")) c = c.substring(0, c.length() - 1);
        return c.isBlank() ? "etc" : c;
    }
}