package com.ol.app.accounts.infrastructure.client.cards;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cards", url = "${cards.url}/api/v1/cards")
public interface CardsClient {

  @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  CardResponse getCard(@RequestParam String phone);

}
