package com.boot.loiteBackend.common.file;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    private String uploadDir;
    private String uploadUrlPrefix;
    // 원격 파일 저장
    private boolean remoteEnabled;
}
