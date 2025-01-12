package com.jpcchaves.emailservice.core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpcchaves.emailservice.core.dto.EventDTO;

import org.springframework.stereotype.Component;

@Component
public class JsonUtil {

    private final ObjectMapper objectMapper;

    public JsonUtil(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String toJson(Object object) {
        try {

            return objectMapper.writeValueAsString(object);
        } catch (Exception e) {

            return "";
        }
    }

    public EventDTO<?> toEvent(String json) {
        try {

            return objectMapper.readValue(json, EventDTO.class);
        } catch (Exception e) {

            return null;
        }
    }
}
