package com.ol.app.accounts.infrastructure.client.cards;

import com.ol.app.accounts.infrastructure.client.ClientConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cards", path = "/api/v1/cards", configuration =  ClientConfig.class)
public interface CardsClient {

  @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  CardResponse getCard(@RequestParam String phone);

}
