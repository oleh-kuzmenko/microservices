package com.ol.app.accounts.infrastructure.client.loans;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "loans", path = "/api/v1/loans")
public interface LoansClient {

  @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  LoanResponse getLoan(@RequestParam String phone);

}
