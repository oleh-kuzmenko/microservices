package com.ol.app.accounts.infrastructure.client;

import com.ol.app.accounts.infrastructure.TraceManager;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;

public class ClientConfig {

  @Bean
  public RequestInterceptor requestInterceptor(TraceManager traceManager) {
    return requestTemplate -> requestTemplate.header(TraceManager.TRACE_ID_HEADER, traceManager.getTraceId());
  }

}
