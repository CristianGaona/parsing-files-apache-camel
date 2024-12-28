package com.crisda24.neoris.parsingfilesapachecamel.processors;


import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class FileTypeDetector implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String fileName = exchange.getIn().getHeader("CamelFileName", String.class);
        String extension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        String fileType;
        switch (extension) {
            case "csv": fileType = "CSV"; break;
            case "json": fileType = "JSON"; break;
            case "xml": fileType = "XML"; break;
            case "xlsx":
            case "xls": fileType = "EXCEL"; break;
            default: fileType = "UNKNOWN"; break;
        }
        exchange.getIn().setHeader("fileType", fileType);
    }
}
