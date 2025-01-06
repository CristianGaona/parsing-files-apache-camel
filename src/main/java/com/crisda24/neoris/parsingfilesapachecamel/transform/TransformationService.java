package com.crisda24.neoris.parsingfilesapachecamel.transform;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class TransformationService {
    public List<Map<String, Object>> transformData(List<Map<String, Object>> inputData) {
        return inputData.stream()
                .map(record -> {
                    Map<String, Object> transformed = new HashMap<>();
                    transformed.put("id", record.get("originalColumn1"));
                    transformed.put("name", record.get("originalColumn2"));
                    transformed.put("value", record.get("originalColumn3"));
                    return transformed;
                })
                .collect(Collectors.toList());
    }

    public String transformDataToJSON(List<Map<String, Object>> data) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            //e.printStackTrace();
            return "Error processing data to JSON";
        }
    }
}
