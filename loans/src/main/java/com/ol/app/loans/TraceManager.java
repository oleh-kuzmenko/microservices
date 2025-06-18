package com.ol.app.loans;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class TraceManager {

  public static final String REQUEST_ID_HEADER = "X-Request-ID";

  public void setTraceId(String requestId) {
    MDC.put(REQUEST_ID_HEADER, requestId);
  }

  public void clearTraceId() {
    MDC.clear();
  }

  public String getTraceId() {
    return MDC.get(REQUEST_ID_HEADER);
  }
}
