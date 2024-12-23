package com.crisda24.neoris.parsingfilesapachecamel.config.redis;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class RedisConnectionVerifier {

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public RedisConnectionVerifier(@Qualifier("defaultReactiveRedisTemplate") ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @PostConstruct
    public void verifyConnection() {
        redisTemplate.opsForValue()
                .set("testKey", "testValue", Duration.ofMinutes(10))
                .flatMap(success -> redisTemplate.opsForValue().get("testKey"))
                .subscribe(
                        value -> log.info("Valor recuperado: {}", value),
                        error -> log.error("Error en la conexión con Redis: {}", error.getMessage())
                );
    }
}
