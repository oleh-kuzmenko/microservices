package com.ol.app.accounts.infrastructure.persistence.audit;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
public class AuditAwareImpl implements AuditorAware<String> {

  private static final String DEFAULT_AUDITOR = "system";

  @Override
  public Optional<String> getCurrentAuditor() {
    return Optional.of(DEFAULT_AUDITOR);
  }
}
