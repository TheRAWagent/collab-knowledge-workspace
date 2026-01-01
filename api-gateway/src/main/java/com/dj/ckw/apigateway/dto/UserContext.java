package com.dj.ckw.apigateway.dto;

import java.util.Map;

public record UserContext(
        String userId,
        String workspaceId
) {
    public static UserContext from(Object raw) {
        // adapt this to your auth-service response
        Map<?, ?> m = (Map<?, ?>) raw;
        return new UserContext(
                m.get("userId").toString(),
                m.get("workspaceId").toString()
        );
    }
}
