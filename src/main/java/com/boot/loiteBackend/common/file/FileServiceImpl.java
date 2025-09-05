package com.boot.loiteBackend.common.file;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    private final FileStorageProperties fileProps;

    // 허용 확장자 화이트리스트
    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of(".jpg", ".jpeg", ".png", ".gif", ".webp", ".pdf", ".svg");

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

            // 경로 탈출 방지: uploadDir 하위인지 검증
            if (!target.startsWith(uploadDir)) {
                throw new SecurityException("잘못된 파일 경로가 감지되었습니다.");
            }

            // 저장 (덮어쓰기 방지)
            // - 같은 이름이 이미 있다면 실패하도록 REPLACE 옵션 미사용
            if (Files.exists(target)) {
                throw new IOException("동일한 파일명이 이미 존재합니다: " + target);
            }

            // 실제 저장
            // - transferTo는 임시파일 이동이므로 성능상 유리
            file.transferTo(target.toFile());

            // URL/물리경로 생성
            String urlPath = fileProps.getUploadUrlPrefix() + "/" + safeCategory + "/" + fileName;
            String physicalPath = target.toString();

            return new FileUploadResult(urlPath, physicalPath);

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
            // 상위에서 처리하고 싶을 수 있으므로 예외 전파
            throw new RuntimeException("파일 삭제 실패: " + physicalPath, e);
        }
    }

    @Override
    public void deleteQuietly(String physicalPath) {
        if (physicalPath == null || physicalPath.isBlank()) return;
        try {
            Files.deleteIfExists(Path.of(physicalPath));
        } catch (Exception ignored) {
            // 로그를 쓰고 싶으면 주입한 로거로 경고만 남기세요.
            // log.warn("deleteQuietly failed: {}", physicalPath, ignored);
        }
    }

    /* ==========================
     * 내부 유틸
     * ========================== */

    private static String extractExtension(String filename) {
        String ext = filename.substring(filename.lastIndexOf(".")).toLowerCase();
        // .jpg 같은 확장자만 허용
        return ext;
    }

    private static String sanitizeFilename(String filename) {
        // 공백 -> _, 위험문자 제거
        String name = filename.replaceAll("\\s+", "_");
        // OS 위험 문자 제거 (필요 시 더 강화)
        name = name.replaceAll("[\\\\/:*?\"<>|]", "");
        // 앞/뒤 점 제거
        name = name.replaceAll("^\\.+", "").replaceAll("\\.+$", "");
        return name;
    }

    private static String sanitizeCategory(String category) {
        if (category == null || category.isBlank()) return "etc";
        // 알파벳/숫자/밑줄/대시/슬래시만 허용
        String c = category.toLowerCase().replaceAll("[^a-z0-9_\\-/]", "");
        // 연속 슬래시 정리
        c = c.replaceAll("/+", "/");
        // 선행/후행 슬래시 제거
        if (c.startsWith("/")) c = c.substring(1);
        if (c.endsWith("/")) c = c.substring(0, c.length() - 1);
        return c.isBlank() ? "etc" : c;
    }
}
