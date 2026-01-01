package com.dj.ckw.apigateway.filter;

import com.dj.ckw.apigateway.service.HttpAuthHandler;
import com.dj.ckw.apigateway.service.WebSocketAuthHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
    private final WebSocketAuthHandler webSocketAuthHandler;
    private final HttpAuthHandler httpAuthHandler;

    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder, @Value("${auth.service.url}") String authServiceUrl, WebSocketAuthHandler webSocketAuthHandler, HttpAuthHandler httpAuthHandler) {
        this.webSocketAuthHandler = webSocketAuthHandler;
        this.httpAuthHandler = httpAuthHandler;
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            boolean isWebSocket =
                    exchange.getRequest().getHeaders()
                            .getFirst(HttpHeaders.UPGRADE) != null;

            if (isWebSocket) {
                return webSocketAuthHandler.handle(exchange, chain);
            }

            return httpAuthHandler.handle(exchange, chain);
        };
    }
}
