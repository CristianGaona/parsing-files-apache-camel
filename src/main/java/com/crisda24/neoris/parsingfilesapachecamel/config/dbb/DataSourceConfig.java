package com.crisda24.neoris.parsingfilesapachecamel.config.dbb;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@Slf4j
public class DataSourceConfig {

    @Bean(name = "connectionDBB")
    @ConfigurationProperties(prefix = "datasource")
    public DataSource connectionDBB(){
        log.info("Estableciendo coxeión a la base de datos");
        return new HikariDataSource();
    }

    @Bean
    @Primary
    public DataSource getConnectionDBB(@Qualifier("connectionDBB") DataSource connectionDBB){
        log.info("Conexión exitosa a la base de datos");
        return connectionDBB;
    }

    @Bean
    public JdbcTemplateAutoConfiguration getJdbcTemplate(@Qualifier("connectionDBB") DataSource connectionDBB){
        return new JdbcTemplateAutoConfiguration();
    }

}
