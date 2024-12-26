package com.crisda24.neoris.parsingfilesapachecamel.config.redis;

import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import io.lettuce.core.TimeoutOptions;
import io.lettuce.core.resource.ClientResources;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@Slf4j
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.password}")
    private String password;

    @Value("${spring.data.redis.timeout}") // Timeout en milisegundos, con valor por defecto
    private long timeout;

    @Bean
    @Qualifier("redisConnectionFactoryCustom")
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        log.info("Estableciendo conexión con Redis...");

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(host, port);

        if (password != null && !password.isEmpty()) {
            lettuceConnectionFactory.setPassword(password);
            log.info("Autenticación con Redis realizada correctamente.");
        } else {
            log.warn("No se configuró contraseña en Redis.");
        }

        return lettuceConnectionFactory;
    }

    @Bean(name = "defaultReactiveRedisTemplate")
    public ReactiveRedisTemplate<String, String> reactiveRedisTemplate(@Qualifier("redisConnectionFactoryCustom") ReactiveRedisConnectionFactory factory) {
        RedisSerializationContext<String, String> context = RedisSerializationContext
                .<String, String>newSerializationContext(new StringRedisSerializer())
                .hashValue(new StringRedisSerializer())
                .hashKey(new StringRedisSerializer())
                .build();

        return new ReactiveRedisTemplate<>(factory, context);
    }
}
