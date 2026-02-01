package com.dj.ckw.apigateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class AuthClient {

  private static final Logger log = LoggerFactory.getLogger(AuthClient.class);

  private final WebClient webClient;

  public AuthClient(WebClient.Builder builder, @Value("${auth.service.url}") String authServiceUrl) {
    this.webClient = builder
        .baseUrl(authServiceUrl)
        .build();
  }

  public Mono<Object> validate(String token) {
    return webClient.get()
        .uri("/validate")
        .header(HttpHeaders.AUTHORIZATION, token)
        .retrieve()
        .bodyToMono(Map.class)
        .handle((body, sink) -> {
          Object ctx = body.get("userContext");
          if (ctx == null) {
            log.warn("Token validation failed: userContext missing in response");
            sink.error(new RuntimeException("Invalid token"));
            return;
          }
          sink.next(ctx);
        })
        .doOnError(e -> log.error("Error during token validation", e));
  }
}
