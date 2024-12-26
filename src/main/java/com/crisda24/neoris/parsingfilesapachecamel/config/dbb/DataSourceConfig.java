package com.crisda24.neoris.parsingfilesapachecamel.config.dbb;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

//@Configuration
@Slf4j
public class DataSourceConfig {

    @Bean(name = "connectionDBB")
    @ConfigurationProperties(prefix = "datasource")
    public DataSource connectionDBB() {
        log.info("Configurando conexión al DataSource");
        return new HikariDataSource();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource connectionDBB) {
        log.info("Creando bean JdbcTemplate para la base de datos");
        return new JdbcTemplate(connectionDBB);
    }
}
