package com.ol.app.cards.rest;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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

  @Getter
  @Setter
  @ConfigurationProperties(prefix = "app")
  public static final class AppInfo {

    private String name;

    private String version;

    private String description;

  }
}
