package com.ol.app.loans.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ManagementController {

  private final AppInfo appInfo;

  @GetMapping("/api/info")
  public AppInfo getAppInfo() {
    return appInfo;
  }

  @ConfigurationProperties(prefix = "app")
  public record AppInfo(String name, String version, String description) {
  }
}
