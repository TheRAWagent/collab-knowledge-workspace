package com.dj.ckw.apigateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class HttpAuthHandler {

  private static final Logger log = LoggerFactory.getLogger(HttpAuthHandler.class);
  private final AuthClient authClient;

  public HttpAuthHandler(AuthClient authClient) {
    this.authClient = authClient;
  }

  public Mono<Void> handle(ServerWebExchange exchange, GatewayFilterChain chain) {

    var cookie = exchange.getRequest().getCookies().getFirst("token");
    if (cookie == null || cookie.getValue().isBlank()) {
      log.warn("Missing auth token in request to {}", exchange.getRequest().getPath());
      exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
      return exchange.getResponse().setComplete();
    }

    return authClient.validate(cookie.getValue())
        .timeout(Duration.ofSeconds(5))
        .flatMap(userToken -> {
          var request = exchange.getRequest()
              .mutate()
              .header("X-User-Info", userToken.toString())
              .build();

          return chain.filter(
              exchange.mutate().request(request).build());
        })
        .onErrorResume(e -> {
          log.error("Authentication failed for request to {}: {}", exchange.getRequest().getPath(), e.getMessage());
          exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
          return exchange.getResponse().setComplete();
        });
  }
}
