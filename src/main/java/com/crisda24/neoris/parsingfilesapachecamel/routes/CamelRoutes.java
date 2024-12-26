package com.crisda24.neoris.parsingfilesapachecamel.routes;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j

@Component
public class CamelRoutes extends RouteBuilder {

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public CamelRoutes(@Qualifier("defaultReactiveRedisTemplate") ReactiveRedisTemplate<String, String> redisTemplate) {
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
        //getContext().addComponent("redis", redisComponent());
        /*from("redis:" + redisHost + ":" + redisPort + "?channels=my-channel&command=SUBSCRIBE")
                .routeId("redis-listener")
                .log("Mensaje recibido de Redis: ${body}")
                .onException(Exception.class)
                .log("Error al recibir el mensaje de Redis: ${exception.message}")
                .handled(true);*/

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


        /*from("direct:processMessage")
                .process(exchange -> {
                    String body = exchange.getIn().getBody(String.class);
                    exchange.getMessage().setBody("Procesado: " + body);
                })
                .log("Mensaje procesado: ${body}");*/
    }

}
