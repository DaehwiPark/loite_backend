package com.boot.loiteBackend.web.payment.config;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "portone.api")
@Getter
@Setter
public class PortOneConfig {
    private String secret;
    private String url;

    @PostConstruct
    public void debugConfig() {
        System.out.println(">>> PortOne API Url = " + url);
        System.out.println(">>> PortOne API Secret = " + secret);
    }
}
