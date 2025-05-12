package com.demo.poc.service;

import com.google.inject.Inject;
import com.demo.poc.properties.PropertiesReader;
import java.time.Duration;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class DriverProviderService {

  private final PropertiesReader propertiesReader;

  private ChromeDriver driver;
  private WebDriverWait webDriverWait;

  public ChromeDriver getChromeDriver() {
    return Optional.ofNullable(driver)
        .orElseGet(() -> {
          System.setProperty("webdriver.chrome.driver", propertiesReader.get().getConfiguration().getDriverPath());
          ChromeOptions options = new ChromeOptions();
          options.addArguments("--remote-allow-origins=*");
          driver = new ChromeDriver(options);
          return driver;
        });
  }

  public WebDriverWait getWebDriverWait() {
    return Optional.ofNullable(webDriverWait)
        .orElseGet(() -> {
          driver = Optional.ofNullable(driver).orElseGet(this::getChromeDriver);
          long waitingTimeMillis = propertiesReader.get().getConfiguration().getWaitingTimeMillis();
          webDriverWait = new WebDriverWait(driver, Duration.ofMillis(waitingTimeMillis));
          return webDriverWait;
        });
  }
}
