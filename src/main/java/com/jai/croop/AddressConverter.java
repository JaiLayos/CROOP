package com.jai.croop;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.Map;
@Converter(autoApply = true)
public class AddressConverter implements AttributeConverter<Map<String, String>, String> {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public String convertToDatabaseColumn(Map<String, String> address) {
        try {
            return objectMapper.writeValueAsString(address);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting address map to JSON", e);
        }
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String addressJson) {
        try {
            return objectMapper.readValue(addressJson, Map.class);
        } catch (IOException e) {
            throw new RuntimeException("Error converting JSON to address map", e);
        }
    }
}
