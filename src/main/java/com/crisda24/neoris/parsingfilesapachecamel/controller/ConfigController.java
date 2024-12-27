package com.crisda24.neoris.parsingfilesapachecamel.controller;

import com.crisda24.neoris.parsingfilesapachecamel.models.Config;
import com.crisda24.neoris.parsingfilesapachecamel.repository.ConfigRepository;
import com.crisda24.neoris.parsingfilesapachecamel.repository.DatabaseTestService;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/config")
public class ConfigController {

    private final ProducerTemplate producerTemplate;

    private final ConfigRepository repository;

    private final DatabaseTestService databaseTestService;

    public ConfigController(ProducerTemplate producerTemplate, ConfigRepository repository, DatabaseTestService databaseTestService) {
        this.producerTemplate = producerTemplate;
        this.repository = repository;
        this.databaseTestService = databaseTestService;
    }

    @PostMapping
    public ResponseEntity<String> publishToRedis(@RequestParam String channel, @RequestBody String message) {
        producerTemplate.sendBodyAndHeader("direct:publishToRedis", message, "channel", channel);
        return ResponseEntity.ok("Mensaje enviado al canal " + channel);
    }

    @PostMapping(path = "/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Config> guardarEntidad(@RequestBody Config miEntidad) {
        return repository.save(miEntidad);  // Llamamos al servicio para guardar el dato
    }

    @GetMapping("/test-db-connection")
    public Mono<String> testDatabaseConnection() {
        return databaseTestService.testConnection()
                .map(result -> "Connection is successful! Result: " + result)
                .defaultIfEmpty("Failed to connect or no result returned");
    }
}
