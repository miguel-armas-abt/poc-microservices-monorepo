package com.demo.poc.spiders;

import com.google.inject.Inject;
import com.demo.poc.service.DriverProviderService;
import com.demo.poc.properties.PropertiesReader;
import com.demo.poc.properties.configuration.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class LoginSpider {

  private final PropertiesReader propertiesReader;
  private final DriverProviderService driverProviderService;

  public void login() throws InterruptedException {
    log.info("start {}", this.getClass().getSimpleName());
    ChromeDriver driver = driverProviderService.getChromeDriver();
    Configuration properties = propertiesReader.get().getConfiguration();
    long waitingTimeMillis = properties.getWaitingTimeMillis();

    driver.get(properties.getLogin().getUri());
    Thread.sleep(waitingTimeMillis*2);

    WebElement adminConsoleButton = driver.findElement(By.linkText("Administration Console"));
    adminConsoleButton.click();
    Thread.sleep(waitingTimeMillis);

    WebElement usernameField = driver.findElement(By.id("username"));
    usernameField.sendKeys(properties.getLogin().getUsername());

    WebElement passwordField = driver.findElement(By.id("password"));
    passwordField.sendKeys(properties.getLogin().getPassword());

    WebElement signInButton = driver.findElement(By.id("kc-login"));
    signInButton.click();
  }
}