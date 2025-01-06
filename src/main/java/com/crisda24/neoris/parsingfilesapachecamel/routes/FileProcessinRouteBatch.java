package com.crisda24.neoris.parsingfilesapachecamel.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;


@Component
public class FileProcessinRouteBatch extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("direct:processFileBatch")
                // Registrar tiempo de inicio
                .process(exchange -> {
                    long startTime = System.currentTimeMillis(); // Registrar inicio
                    exchange.setProperty("startTime", startTime);
                    System.out.println("Inicio del proceso: " + startTime);
                })
                .split(body().tokenize("\n"))
                .streaming()
                .process(exchange -> {
                    String line = exchange.getIn().getBody(String.class).trim();
                    if (!line.equalsIgnoreCase("id,name,age,email")) {
                        String[] fields = line.split(",");
                        String transformedLine = String.join("|", fields) + "\n";
                        exchange.getIn().setBody(transformedLine);
                    } else {
                        exchange.getIn().setBody(null); // Descarta encabezado
                    }
                })
                .filter(exchange -> exchange.getIn().getBody() != null)
                .to("file://src/main/resources/output?fileName=cien_mil.txt&fileExist=Append")
                // Registrar tiempo de fin y calcular duración
                .process(exchange -> {
                    long endTime = System.currentTimeMillis(); // Registrar fin
                    long startTime = (long) exchange.getProperty("startTime");
                    long duration = endTime - startTime;
                    System.out.println("Tiempo total del proceso: " + duration + " ms");
                });

        /*from("direct:processFileBatch2").process(exchange -> {
            long startTime = System.currentTimeMillis();
            exchange.setProperty("startTime", startTime);
            System.out.println("Inicio del proceso: " + startTime);
        }).split(body().tokenize("\n")).streaming().to("bean:batchJobLauncher?method=startJob").process(exchange -> {
            long endTime = System.currentTimeMillis();
            long startTime = (long) exchange.getProperty("startTime");
            long duration = endTime - startTime;
            System.out.println("Tiempo total del proceso: " + duration + " ms");
        });*/
    }
}