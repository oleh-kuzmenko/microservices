package com.ol.app.accounts.infrastructure;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Component
public class TraceManager {

  public static final String TRACE_ID_HEADER = "X-Request-ID";
  private static final String TRACE_ID_MDC_KEY = "traceId";

  public void setTraceId(String requestId) {
    MDC.put(TRACE_ID_MDC_KEY, requestId);
  }

  public void clearTraceId() {
    MDC.remove(TRACE_ID_MDC_KEY);
  }

  public String getTraceId() {
    return MDC.get(TRACE_ID_MDC_KEY);
  }
}
