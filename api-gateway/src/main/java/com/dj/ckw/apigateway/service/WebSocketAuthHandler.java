package com.dj.ckw.apigateway.service;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class WebSocketAuthHandler {

    private final AuthClient authClient;

    public WebSocketAuthHandler(AuthClient authClient) {
        this.authClient = authClient;
    }

    public Mono<Void> handle(ServerWebExchange exchange, GatewayFilterChain chain) {

        var cookie = exchange.getRequest().getCookies().getFirst("token");
        if (cookie == null || cookie.getValue().isBlank()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        return authClient.validate(cookie.getValue())
                .timeout(Duration.ofSeconds(2)) // CRITICAL
                .flatMap(userToken -> {
                    var request = exchange.getRequest()
                            .mutate()
                            .header("X-User-Info", userToken.toString())
                            .build();

                    return chain.filter(
                            exchange.mutate().request(request).build()
                    );
                })
                .onErrorResume(e -> {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                });
    }
}
