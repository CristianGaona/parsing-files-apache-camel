package com.crisda24.neoris.parsingfilesapachecamel.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DatabaseTestService {
    @Autowired
    private DatabaseClient databaseClient;

    public Mono<Integer> testConnection() {
        // Ejecuta un SELECT 1 para probar la conexión
        return databaseClient.sql("SELECT 1")
                .map(row -> row.get(0, Integer.class))
                .one();
    }
}
