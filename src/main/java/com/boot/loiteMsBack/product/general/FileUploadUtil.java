package com.boot.loiteMsBack.product.general;

import org.springframework.security.core.parameters.P;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class FileUploadUtil {

    private static final String BASE_DIR = "src/main/static/assets/image/product/";

    // 허용할 확장자 목록
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".jpg", ".jpeg", ".png", ".gif", ".webp");

    public static String saveFile(MultipartFile file) throws IOException {
        File dir = new File(BASE_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String originalFilename = file.getOriginalFilename();

        // 1. 파일명 null 또는 확장자 없는 경우 방어
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IllegalArgumentException("유효하지 않은 파일명입니다.");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();

        // 2. 확장자 유효성 검사
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("지원하지 않는 이미지 형식입니다: " + extension);
        }

        String savedFilename = UUID.randomUUID() + extension;

        File savepath = new File(dir, savedFilename);
        file.transferTo(savepath);

        return "/assets/image/product/" + savedFilename;
    }
}

