package com.crisda24.neoris.parsingfilesapachecamel.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;


import lombok.extern.slf4j.Slf4j;

@Slf4j

@Component
public class RedisRoute extends RouteBuilder {

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public RedisRoute(@Qualifier("defaultReactiveRedisTemplate") ReactiveRedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;



    @Override
    public void configure() throws Exception{
        onException(Exception.class)
                .process(new Processor() {

                    @Override
                    public void process(Exchange exchange) throws Exception {
                        Throwable caught = exchange.getProperty(Exchange.EXCEPTION_CAUGHT,
                                Throwable.class);
                        log.error("FATAL ERROR - ", caught);
                    }
                })
                .handled(true);

        from("direct:publishToRedis")
                .routeId("redisPublishRoute")
                .log("Publicando mensaje: ${body} en canal: ${header.channel}")
                .process(exchange -> {
                    String channel = exchange.getIn().getHeader("channel", String.class);
                    String message = exchange.getIn().getBody(String.class);

                    redisTemplate
                            .convertAndSend(channel, message)
                            .doOnNext(count -> exchange.getIn().setHeader("redisResult", count))
                            .doOnError(throwable -> exchange.getIn().setHeader("redisError", throwable.getMessage()))
                            .subscribe();
                })
                .log("Mensaje enviado correctamente, suscriptores notificados: ${header.redisResult}")
                .onException(Exception.class)
                .log("Error enviando mensaje a Redis: ${header.redisError}")
                .handled(true)
                .end();
    }

}
