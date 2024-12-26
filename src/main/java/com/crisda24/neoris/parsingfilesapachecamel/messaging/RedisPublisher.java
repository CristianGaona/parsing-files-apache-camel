package com.crisda24.neoris.parsingfilesapachecamel.messaging;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RedisPublisher {
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public RedisPublisher(@Qualifier("defaultReactiveRedisTemplate") ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Mono<Boolean> publish(String channel, String message) {
        return redisTemplate.convertAndSend(channel, message)
                .map(count -> count > 0);
    }
}
