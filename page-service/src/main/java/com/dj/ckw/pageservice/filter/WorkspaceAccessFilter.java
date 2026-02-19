package com.dj.ckw.pageservice.filter;

import java.util.List;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class WorkspaceAccessFilter implements WebFilter {

  private static final String AUTHORIZATION_HEADER = "X-Page-Authorization";

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    HttpMethod method = exchange.getRequest().getMethod();
    String accessHeader = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER);
    List<String> allowedPaths = List.of("/v3/api-docs", "/swagger", "/actuator");
    String path = exchange.getRequest().getPath().toString();
    if (allowedPaths.stream().anyMatch(path::startsWith)) {
      return chain.filter(exchange);
    }

    if (accessHeader == null) {
      exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
      return exchange.getResponse().setComplete();
    }

    if ("INTERNAL".equals(accessHeader)) {
      // Always allow internal service access
      return chain.filter(exchange);
    }

    // Always allow GET
    if (method == HttpMethod.GET) {
      return chain.filter(exchange);
    }

    if ("READER".equals(accessHeader)) {
      exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.FORBIDDEN);
      return exchange.getResponse().setComplete();
    } else {
      return chain.filter(exchange);
    }
  }
}
