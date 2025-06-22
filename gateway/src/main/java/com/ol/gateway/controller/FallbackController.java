package com.ol.gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

  @GetMapping("/fallback")
  Mono<String> fallback() {
    return Mono.just("An error occurred, please try again later");
  }
}
