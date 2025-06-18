package com.ol.app.accounts.infrastructure.rest.filter;

import java.io.IOException;

import com.ol.app.accounts.infrastructure.TraceManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestIdFilter extends OncePerRequestFilter {

  private final TraceManager traceManager;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String header = request.getHeader(TraceManager.TRACE_ID_HEADER);
      traceManager.setTraceId(header);
      filterChain.doFilter(request, response);
    } finally {
      traceManager.clearTraceId();
    }
  }
}
