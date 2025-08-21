package com.boot.loiteBackend.config;

import com.boot.loiteBackend.common.file.FileStorageProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final FileStorageProperties fileProps;

    public WebConfig(FileStorageProperties fileProps) {
        this.fileProps = fileProps;
    }

    // CORS 허용 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://localhost:5173",
                        "http://localhost:3000",
                        "https://www.loite.co.kr",
                        "https://loite.co.kr",
                        "https://admin.loite.co.kr",
                        "http://192.168.0.73:5173"
                )
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true); // 필요 시 쿠키 전달 허용
    }

    // 정적 리소스 핸들링 (ex: 업로드 이미지 경로)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectRoot = System.getProperty("user.dir");
        File uploadsDir = new File(projectRoot, fileProps.getUploadDir());
        String absolutePath = uploadsDir.getAbsolutePath().replace("\\", "/");

        registry.addResourceHandler(fileProps.getUploadUrlPrefix() + "/**")
                .addResourceLocations("file:" + absolutePath + "/");
    }
}
