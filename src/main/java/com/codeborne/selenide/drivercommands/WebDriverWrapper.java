package com.codeborne.selenide.drivercommands;

import com.codeborne.selenide.Browser;
import com.codeborne.selenide.Config;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.proxy.SelenideProxyServer;
import org.openqa.selenium.WebDriver;
import org.testng.internal.Nullable;

import javax.annotation.Nonnull;
import java.util.logging.Logger;

import static java.util.Objects.requireNonNull;

/**
 * A `Driver` implementation which uses given webdriver [and proxy].
 * It doesn't open a new browser.
 * It doesn't start a new proxy.
 */
public class WebDriverWrapper implements Driver {
  private static final Logger log = Logger.getLogger(WebDriverWrapper.class.getName());

  private final Config config;
  private final WebDriver webDriver;
  private final SelenideProxyServer selenideProxy;
  private final BrowserHealthChecker browserHealthChecker;

  public WebDriverWrapper(@Nonnull Config config, @Nonnull WebDriver webDriver, @Nullable SelenideProxyServer selenideProxy) {
    this(config, webDriver, selenideProxy, new BrowserHealthChecker());
  }

  WebDriverWrapper(@Nonnull Config config, @Nonnull WebDriver webDriver, @Nullable SelenideProxyServer selenideProxy,
                   @Nonnull BrowserHealthChecker browserHealthChecker) {
    requireNonNull(config, "config must not be null");
    requireNonNull(webDriver, "webDriver must not be null");

    this.config = config;
    this.webDriver = webDriver;
    this.selenideProxy = selenideProxy;
    this.browserHealthChecker = browserHealthChecker;
  }

  @Override
  public Config config() {
    return config;
  }

  @Override
  public Browser browser() {
    return new Browser(config.browser(), config.headless());
  }

  @Override
  public boolean hasWebDriverStarted() {
    return webDriver != null;
  }

  @Override
  public WebDriver getWebDriver() {
    return webDriver;
  }

  @Override
  public SelenideProxyServer getProxy() {
    return selenideProxy;
  }

  @Override
  public WebDriver getAndCheckWebDriver() {
    if (webDriver != null && !browserHealthChecker.isBrowserStillOpen(webDriver)) {
      log.info("Webdriver has been closed meanwhile");
      close();
      return null;
    }
    return webDriver;
  }

  /**
   * Close the webdriver.
   *
   * NB! The behaviour was changed in Selenide 5.4.0
   * Even if webdriver was created by user - it will be closed.
   * It may hurt if you try to use this browser after closing.
   */
  @Override
  public void close() {
    if (!config.holdBrowserOpen()) {
      new CloseDriverCommand(webDriver, selenideProxy).run();
    }
  }
}
