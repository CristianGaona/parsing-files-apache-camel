package com.crisda24.neoris.parsingfilesapachecamel.routes;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.spi.TypeConverterRegistry;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomCamelConfig {
    @Bean
    CamelContextConfiguration contextConfiguration() {

        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext context) {
                //context.setLoadHealthChecks(true);
                context.getManagementStrategy().addEventNotifier(new StartStopEventNotifier());
                TypeConverterRegistry typeConverterRegistry = context.getTypeConverterRegistry();
            }

            @Override
            public void afterApplicationStart(CamelContext camelContext) {

            }
        };
    }
}
