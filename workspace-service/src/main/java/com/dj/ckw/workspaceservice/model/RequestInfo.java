package com.dj.ckw.workspaceservice.model;

import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.exc.JsonNodeException;

@Component
@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RequestInfo {
    private final ObjectMapper objectMapper;
    private String email;

    public RequestInfo(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String decodedJson) throws JsonNodeException {
        JsonNode idNode = this.objectMapper.readTree(decodedJson).get("id");
        this.email = idNode != null && !idNode.isNull() ? idNode.asString() : null;
    }

}
