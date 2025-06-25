package com.ol.app.accounts.infrastructure.client.loans;

import com.ol.app.accounts.infrastructure.client.ClientConfig;
import com.ol.app.accounts.infrastructure.client.loans.LoansClient.Fallback;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RateLimiter(name = "loans")
@FeignClient(name = "loans", path = "/api/v1/loans", configuration =  ClientConfig.class, fallback = Fallback.class)
public interface LoansClient {

  @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  LoanResponse getLoan(@RequestParam String phone);

  @Component
  class Fallback implements LoansClient {

    @Override
    public LoanResponse getLoan(String phone) {
      return new LoanResponse(null, null, null, null, null, null, null, null, null, null, null);
    }
  }
}
