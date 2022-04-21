package com.nhnacademy.group6Project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;

public class JsonToMap {
    private String jsonString;

    public JsonToMap(String jsonString) {
        this.jsonString = jsonString;
    }

    public Map<String, String> parsing() {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, String> map = null;
        try {
            map = objectMapper.readValue(jsonString, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return map;
    }
}
