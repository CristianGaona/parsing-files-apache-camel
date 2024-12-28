package com.crisda24.neoris.parsingfilesapachecamel.controller;

import com.crisda24.neoris.parsingfilesapachecamel.models.Config;
import com.crisda24.neoris.parsingfilesapachecamel.repository.ConfigRepository;
import com.crisda24.neoris.parsingfilesapachecamel.repository.DatabaseTestService;
import com.crisda24.neoris.parsingfilesapachecamel.routes.FileProcessingRoute;
import org.apache.camel.ProducerTemplate;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Iterator;

@RestController
@RequestMapping("/config")
public class ConfigController {

    private final ProducerTemplate producerTemplate;

    private final ConfigRepository repository;

    private final DatabaseTestService databaseTestService;

    private final FileProcessingRoute fileProcessingRoute;

    public ConfigController(ProducerTemplate producerTemplate, ConfigRepository repository, DatabaseTestService databaseTestService, FileProcessingRoute fileProcessingRoute) {
        this.producerTemplate = producerTemplate;
        this.repository = repository;
        this.databaseTestService = databaseTestService;
        this.fileProcessingRoute = fileProcessingRoute;
    }

    /*@PostMapping
    public ResponseEntity<String> publishToRedis(@RequestParam String channel, @RequestBody String message) {
        producerTemplate.sendBodyAndHeader("direct:publishToRedis", message, "channel", channel);
        return ResponseEntity.ok("Mensaje enviado al canal " + channel);
    }*/

    /*@PostMapping(path = "/save")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Config> guardarEntidad(@RequestBody Config miEntidad) {
        return repository.save(miEntidad);  // Llamamos al servicio para guardar el dato
    }*/

    @GetMapping("/test-db-connection")
    public Mono<String> testDatabaseConnection() {
        return databaseTestService.testConnection()
                .map(result -> "Connection is successful! Result: " + result)
                .defaultIfEmpty("Failed to connect or no result returned");
    }

    @PostMapping(path = "/upload")
    public Mono<ResponseEntity<String>> uploadFile(@RequestPart("file") FilePart file) {
        return file.content()
                .collectList()
                .flatMap(dataBuffers -> {
                    try {
                        InputStream inputStream = new SequenceInputStream(
                                new Enumeration<InputStream>() {
                                    private final Iterator<DataBuffer> iterator = dataBuffers.iterator();

                                    @Override
                                    public boolean hasMoreElements() {
                                        return iterator.hasNext();
                                    }

                                    @Override
                                    public InputStream nextElement() {
                                        return new ByteArrayInputStream(
                                                iterator.next().asByteBuffer().array()
                                        );
                                    }
                                }
                        );

                        producerTemplate.sendBodyAndHeader(
                                "direct:processFile",
                                inputStream,
                                "CamelFileName",
                                file.filename()
                        );

                        inputStream.close();

                        return Mono.just(ResponseEntity.ok("File processed successfully"));
                    } catch (IOException e) {
                        return Mono.error(new RuntimeException("Error processing file: " + e.getMessage(), e));
                    }
                });
    }
}
