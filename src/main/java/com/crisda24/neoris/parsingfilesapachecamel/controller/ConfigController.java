package com.crisda24.neoris.parsingfilesapachecamel.controller;

import com.crisda24.neoris.parsingfilesapachecamel.models.Config;
import com.crisda24.neoris.parsingfilesapachecamel.repository.ConfigRepository;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/config")
public class ConfigController {

    private final ProducerTemplate producerTemplate;

    private final ConfigRepository repository;

    public ConfigController(ProducerTemplate producerTemplate, ConfigRepository repository) {
        this.producerTemplate = producerTemplate;
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<String> publishToRedis(@RequestParam String channel, @RequestBody String message) {
        producerTemplate.sendBodyAndHeader("direct:publishToRedis", message, "channel", channel);
        return ResponseEntity.ok("Mensaje enviado al canal " + channel);
    }
}
