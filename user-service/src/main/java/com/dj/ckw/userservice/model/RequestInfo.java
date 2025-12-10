package com.dj.ckw.userservice.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class RequestInfo {
    private final ObjectMapper objectMapper;
    private String email;

    public RequestInfo(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setEmail(String decodedJson) throws JsonProcessingException {
        JsonNode idNode = this.objectMapper.readTree(decodedJson).get("id");
        this.email = idNode != null && !idNode.isNull() ? idNode.asText() : null;
    }

    public String getEmail() {
        return email;
    }
}
