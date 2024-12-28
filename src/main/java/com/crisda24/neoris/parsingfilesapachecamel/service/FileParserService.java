package com.crisda24.neoris.parsingfilesapachecamel.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class FileParserService {
    public List<Map<String, Object>> parseJSON(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
    }

    public List<Map<String, Object>> parseCSV(String fileContent) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        String[] lines = fileContent.split("\n");

        // Obtener las cabeceras (columnas)
        String[] headers = lines[0].split(",");

        // Parsear cada fila y convertir a un mapa de columnas
        for (int i = 1; i < lines.length; i++) {
            String[] values = lines[i].split(",");
            Map<String, Object> rowData = new HashMap<>();
            for (int j = 0; j < headers.length; j++) {
                rowData.put(headers[j], values[j]);
            }
            dataList.add(rowData);
        }

        return dataList;
    }
}
