package com.java.buddies.config;

import java.util.Optional;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ScrapingConfig {

  private static ChromeDriver chromeDriver;

  public static ChromeDriver getDriver() {
    return Optional.ofNullable(chromeDriver)
        .orElseGet(() -> {
          System.setProperty("webdriver.chrome.driver", "src/main/resources/drivers/chromedriver.exe");
          ChromeOptions options = new ChromeOptions();
          options.addArguments("--remote-allow-origins=*");
          chromeDriver = new ChromeDriver(options);
          return chromeDriver;
        });
  }
}
