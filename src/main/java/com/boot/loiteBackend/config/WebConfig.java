package com.boot.loiteBackend.config;

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


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5173")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String projectRoot = System.getProperty("user.dir");
        File uploadsDir = new File(projectRoot, fileProps.getUploadDir());
        String absolutePath = uploadsDir.getAbsolutePath().replace("\\", "/");
        registry.addResourceHandler(fileProps.getUploadUrlPrefix() + "/**")
                .addResourceLocations("file:" + absolutePath + "/");
    }
}