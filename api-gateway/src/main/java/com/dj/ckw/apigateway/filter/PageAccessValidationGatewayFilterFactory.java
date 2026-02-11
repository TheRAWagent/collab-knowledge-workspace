package com.dj.ckw.apigateway.filter;

import com.dj.ckw.apigateway.grpc.PageAccessRequest;
import com.dj.ckw.apigateway.service.WorkspaceServiceGrpcClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.exc.JsonNodeException;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class PageAccessValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<AbstractGatewayFilterFactory.NameConfig> {
    private final WorkspaceServiceGrpcClient workspaceServiceGrpcClient;
    private final JsonMapper objectMapper;

    public PageAccessValidationGatewayFilterFactory(WorkspaceServiceGrpcClient workspaceServiceGrpcClient, JsonMapper objectMapper) {
        super(NameConfig.class);
        this.workspaceServiceGrpcClient = workspaceServiceGrpcClient;
        this.objectMapper = objectMapper;
    }

    @Override
    public GatewayFilter apply(NameConfig config) {
        return (exchange, chain) -> {
            String userInfo = exchange.getRequest().getHeaders().getFirst("X-User-Info");
            if (userInfo == null) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String userId;
            try {
                JsonNode node = objectMapper.readTree(
                        Base64.getDecoder().decode(userInfo.getBytes(StandardCharsets.UTF_8))
                );
                userId = node.get("id").asText();
            } catch (JsonNodeException e) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String workspaceId = exchange.getRequest().getURI().getPath().split("/")[2];

            PageAccessRequest request = PageAccessRequest.newBuilder()
                    .setWorkspaceId(workspaceId)
                    .setUserId(userId)
                    .build();

            // Call gRPC FutureStub
            return workspaceServiceGrpcClient.checkPageAccess(request)
                    .flatMap(response -> {
                        if (!response.getHasAccess()) {
                            exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                            return exchange.getResponse().setComplete();
                        }

                        ServerHttpRequest mutatedRequest = exchange.getRequest().mutate()
                                .header("X-Page-Authorization", response.getWorkspaceMemberRole().toString())
                                .build();

                        ServerWebExchange mutatedExchange = exchange.mutate()
                                .request(mutatedRequest)
                                .build();

                        return chain.filter(mutatedExchange);
                    })
                    .onErrorResume(ex -> {
                        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                        return exchange.getResponse().setComplete();
                    });


        };
    }
}
