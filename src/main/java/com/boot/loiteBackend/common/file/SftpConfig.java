package com.boot.loiteBackend.common.file;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ FileStorageProperties.class, SftpProperties.class })
public class SftpConfig {

    @Bean
    @ConditionalOnProperty(prefix = "file", name = "remote-enabled", havingValue = "true")
    public SftpUploader sftpUploader(SftpProperties props) {
        return new SftpUploader(props);
    }
}