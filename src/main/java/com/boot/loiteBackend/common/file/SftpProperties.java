package com.boot.loiteBackend.common.file;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Getter
@Setter
@ConfigurationProperties(prefix = "sftp")
public class SftpProperties {
    private String host;
    private int port = 22;
    private String user;
    private String password;
}