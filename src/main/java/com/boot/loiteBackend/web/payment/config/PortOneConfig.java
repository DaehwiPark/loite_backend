package com.boot.loiteBackend.web.payment.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "portone.api")
@Getter
@Setter
public class PortOneConfig {
    private String key;
    private String secret;
    private String url;
}
