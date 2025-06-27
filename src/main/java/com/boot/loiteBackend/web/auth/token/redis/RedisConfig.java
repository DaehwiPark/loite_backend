package com.boot.loiteBackend.web.auth.token.redis;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories(basePackages = "com.boot.loiteBackend.web.auth.token.redis")
public class RedisConfig {
}
