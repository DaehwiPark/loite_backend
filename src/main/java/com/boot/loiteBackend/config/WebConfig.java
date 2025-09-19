package com.boot.loiteBackend.config;

import com.boot.loiteBackend.common.file.FileStorageProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final FileStorageProperties fileProps;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "http://localhost:5173",
                        "http://localhost:3000",
                        "http://192.168.0.73:5173",
                        "https://loite.co.kr",
                        "https://www.loite.co.kr",
                        "https://admin.loite.co.kr",
                        "https://api.loite.co.kr"
                )
                .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS","HEAD")
                .allowedHeaders("*")
                .allowCredentials(true)
                .exposedHeaders("Authorization", "X-Filename");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String raw = fileProps.getUploadDir();
        if (raw == null || raw.isBlank()) {
            throw new IllegalStateException("file.upload-dir is not set");
        }

        // 눈에 안 보이는 공백/제어문자 제거
        String cleaned = raw.trim();

        Path configured = Paths.get(cleaned);
        Path uploadRoot = configured.isAbsolute()
                ? configured.normalize()
                : Paths.get(System.getProperty("user.dir")).resolve(configured).normalize();

        String pattern  = fileProps.getUploadUrlPrefix() + "/**";  // "/uploads/**"
        String location = uploadRoot.toUri().toString();           // "file:/home/.../uploads/"

        registry.addResourceHandler(pattern)
                .addResourceLocations(location);

        System.out.println("[Static] " + pattern + " -> " + location + " (raw='" + raw + "')");
    }

}