package com.codeborne.selenide.webdriver;

import com.codeborne.selenide.Browser;
import com.codeborne.selenide.Config;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class EdgeDriverFactory extends AbstractDriverFactory {
  private static final Logger log = LoggerFactory.getLogger(EdgeDriverFactory.class);

  @Override
  WebDriver create(Config config, Proxy proxy) {
    return createEdgeDriver(config, proxy);
  }

  @Override
  boolean supports(Config config, Browser browser) {
    return browser.isEdge();
  }

  private WebDriver createEdgeDriver(Config config, Proxy proxy) {
    DesiredCapabilities capabilities = createCommonCapabilities(config, proxy);
    EdgeOptions options = new EdgeOptions();
    options.merge(capabilities);
    if (!config.browserBinary().isEmpty()) {
      log.info("Using browser binary: {}", config.browserBinary());
      log.warn("Changing browser binary not supported in Edge, setting will be ignored.");
    }
    return new EdgeDriver(options);
  }
}
