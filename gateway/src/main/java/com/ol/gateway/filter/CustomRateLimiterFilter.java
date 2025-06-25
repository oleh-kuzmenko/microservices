package com.ol.gateway.filter;

import java.net.InetSocketAddress;
import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class CustomRateLimiterFilter implements GlobalFilter {

  private static final Logger logger = LoggerFactory.getLogger(CustomRateLimiterFilter.class);

  private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

  private final Bandwidth limit;

  public CustomRateLimiterFilter() {
    this.limit = Bandwidth.builder()
        .capacity(3)
        .refillIntervally(3, Duration.ofSeconds(10))
        .build();
  }

  private Bucket resolveBucket(String key) {
    return cache.computeIfAbsent(key, this::newBucket);
  }

  private Bucket newBucket(String apiKey) {
    return Bucket.builder()
        .addLimit(limit)
        .build();
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    var remoteAddrOptional = Optional.ofNullable(exchange.getRequest().getRemoteAddress())
        .map(InetSocketAddress::getHostString);
    if (remoteAddrOptional.isEmpty()) {
      logger.warn("Remote address not found");
      exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
      return exchange.getResponse().setComplete();
    }
    String remoteAddr = remoteAddrOptional.get();
    Bucket bucket = resolveBucket(remoteAddr);

    if (bucket.tryConsume(1)) {
      return chain.filter(exchange);
    } else {
      logger.warn("Too many requests from {}", remoteAddr);
      exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
      return exchange.getResponse().setComplete();
    }
  }
}
