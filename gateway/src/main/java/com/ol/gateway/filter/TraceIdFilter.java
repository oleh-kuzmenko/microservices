package com.ol.gateway.filter;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdFilter implements GlobalFilter {

  private static final Logger log = LoggerFactory.getLogger(TraceIdFilter.class);

  private static final String TRACE_ID_HEADER = "X-Request-ID";

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    String requestId = UUID.randomUUID().toString();
    ServerHttpRequest request = exchange.getRequest();
    ServerHttpResponse response = exchange.getResponse();

    log.info("Request: {} {} {}", requestId, request.getMethod(), request.getURI());

    response.getHeaders().set(TRACE_ID_HEADER, requestId);
    ServerHttpRequest tracedRequest = request.mutate().header(TRACE_ID_HEADER, requestId).build();

    return chain.filter(exchange.mutate().request(tracedRequest).build())
        .then(Mono.fromRunnable(() -> log.info("Response: {} {}", requestId, response.getStatusCode())));
  }
}
