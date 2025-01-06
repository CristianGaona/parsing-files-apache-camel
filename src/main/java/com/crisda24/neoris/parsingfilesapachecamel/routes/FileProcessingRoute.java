package com.crisda24.neoris.parsingfilesapachecamel.routes;

import com.crisda24.neoris.parsingfilesapachecamel.processors.FileTypeDetector;
import com.crisda24.neoris.parsingfilesapachecamel.service.FileParserService;
import com.crisda24.neoris.parsingfilesapachecamel.transform.TransformationService;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FileProcessingRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("direct:processFile")
                .process(exchange -> {
                    String body = exchange.getIn().getBody(String.class);
                    body = body.replaceAll("\r", "");
                    exchange.getIn().setBody(body);
                })
                .routeId("genericFileProcessor")
                .log("Processing file: ${header.CamelFileName}")
                .process(new FileTypeDetector())
                .log("File type detected: ${header.fileType}")
                .choice()
                .when(header("fileType").isEqualTo("CSV"))
                .bean(FileParserService.class, "parseCSV")
                .when(header("fileType").isEqualTo("JSON"))
                .bean(FileParserService.class, "parseJSON")
                .otherwise()
                .log("Unsupported file type: ${header.fileType}")
                .end()
                .bean(TransformationService.class, "transformDataToJSON")
                .to("file://src/main/resources/output?fileName=processed-output.json")
                .log("File processed and saved to output folder.");

        
    }
}
