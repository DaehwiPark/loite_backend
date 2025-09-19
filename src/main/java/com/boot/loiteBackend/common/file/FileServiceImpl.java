package com.boot.loiteBackend.common.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileStorageProperties fileProps;
    private final Optional<SftpUploader> sftpUploader;

    // 허용 확장자 화이트리스트
    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of(".jpg", ".jpeg", ".png", ".gif", ".webp", ".pdf", ".svg", ".mp4");

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
        String cleanName = sanitizeFilename(Objects.requireNonNull(originalName));
        String fileName = UUID.randomUUID() + "_" + cleanName;

        try {
            // URL(클라이언트 접근 경로)
            String urlPath = normalizeUrl(fileProps.getUploadUrlPrefix())
                             + "/" + safeCategory + "/" + fileName;

            if (fileProps.isRemoteEnabled()) {
                // ===== SFTP 원격 저장 =====
                if (sftpUploader.isEmpty()) {
                    throw new IllegalStateException("원격 저장이 활성화되어 있지만 SFTP 업로더 빈이 없습니다. (file.remote-enabled=true 인지, 조건부 Bean 등록을 확인하세요)");
                }

                String remoteRoot = normalizeUnix(fileProps.getUploadDir());  // 예: /home/loite/springboot/uploads
                String remoteDir  = remoteRoot + "/" + safeCategory;          // 예: /home/.../uploads/etc

                sftpUploader.get().upload(file, remoteDir, fileName);         // 서버에 업로드

                String physicalPath = remoteDir + "/" + fileName;             // 서버 실제 경로(로그/DB)
                long size = file.getSize();
                String contentType = file.getContentType();

                return new FileUploadResult(urlPath, physicalPath, fileName, size, contentType);

            } else {
                // ===== 로컬(또는 마운트된 경로) 저장 =====
                String configured = fileProps.getUploadDir();
                Path base = Paths.get(configured);
                if (!base.isAbsolute()) {
                    base = Paths.get(System.getProperty("user.dir"), configured);
                }
                Path uploadDir = base.resolve(safeCategory).toAbsolutePath().normalize();

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

                String physicalPath = target.toString();
                long size = file.getSize();
                String contentType = file.getContentType();

                return new FileUploadResult(urlPath, physicalPath, fileName, size, contentType);
            }

        } catch (IOException e) {
            log.error("파일 저장 실패: {}", e.getMessage(), e);
            throw new RuntimeException("파일 저장 실패", e);
        } catch (Exception e) {
            // SFTP 등 기타 예외
            log.error("파일 원격 저장 실패: {}", e.getMessage(), e);
            throw new RuntimeException("파일 원격 저장 실패", e);
        }
    }

    @Override
    public boolean delete(String physicalPath) {
        if (physicalPath == null || physicalPath.isBlank()) {
            return false;
        }
        try {
            if (fileProps.isRemoteEnabled()) {
                if (sftpUploader.isEmpty()) {
                    throw new IllegalStateException("원격 삭제가 활성화되어 있지만 SFTP 업로더 빈이 없습니다.");
                }
                return sftpUploader.get().delete(normalizeUnix(physicalPath));
            } else {
                return Files.deleteIfExists(Path.of(physicalPath));
            }
        } catch (Exception e) {
            throw new RuntimeException("파일 삭제 실패: " + physicalPath, e);
        }
    }

    @Override
    public void deleteQuietly(String physicalPath) {
        if (physicalPath == null || physicalPath.isBlank()) return;
        try {
            if (fileProps.isRemoteEnabled()) {
                if (sftpUploader.isPresent()) {
                    sftpUploader.get().delete(normalizeUnix(physicalPath));
                } else {
                    log.warn("원격 삭제가 활성화되어 있지만 SFTP 업로더 빈이 없어 삭제를 건너뜀: {}", physicalPath);
                }
            } else {
                Files.deleteIfExists(Path.of(physicalPath));
            }
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

    private static String normalizeUnix(String p) {
        return p == null ? "" : p.replace("\\", "/").replaceAll("/+$", "");
    }

    private static String normalizeUrl(String p) {
        // "/uploads" 또는 "https://loite.co.kr/uploads" 모두 처리
        String x = normalizeUnix(p);
        return x.startsWith("http") ? x : (x.startsWith("/") ? x : "/" + x);
    }
}