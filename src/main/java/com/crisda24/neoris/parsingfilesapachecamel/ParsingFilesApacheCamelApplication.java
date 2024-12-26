package com.crisda24.neoris.parsingfilesapachecamel;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootApplication
public class ParsingFilesApacheCamelApplication {

    public static void main(String[] args) {
        SpringApplication.run(ParsingFilesApacheCamelApplication.class, args);
    }

    /*@Bean
    public CommandLineRunner testDatabaseConnection(JdbcTemplate jdbcTemplate) {
        return args -> {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            if (result != null && result == 1) {
                System.out.println("Conexión a la base de datos exitosa.");
            } else {
                System.err.println("Error al verificar la conexión con la base de datos.");
            }
        };
    }*/

}
