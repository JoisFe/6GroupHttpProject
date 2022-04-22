package com.nhnacademy.group6Project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonSerialization {
    private ResponseMessageBody responseMessageBody;

    public JsonSerialization(ResponseMessageBody responseMessageBody) {
        this.responseMessageBody = responseMessageBody;
    }

    public String serialize() {
        ObjectMapper objectMapper = new ObjectMapper();

        String jsonString = null;
        try {
            jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(responseMessageBody);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return jsonString;

    }
}
