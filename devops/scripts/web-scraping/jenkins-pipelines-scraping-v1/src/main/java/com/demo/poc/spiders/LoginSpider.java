package com.demo.poc.spiders;

import com.demo.poc.properties.PropertiesReader;
import com.demo.poc.properties.configuration.Configuration;
import com.demo.poc.service.DriverProviderService;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class LoginSpider {

  private final PropertiesReader propertiesReader;
  private final DriverProviderService driverProviderService;

  public void loginIfNotPresent() throws InterruptedException {
    if (!isLoginScreenPresent()) {
      Configuration properties = propertiesReader.get().getConfiguration();
      ChromeDriver driver = driverProviderService.getChromeDriver();
      long waitingTimeMillis = properties.getWaitingTimeMillis();

      driver.get(properties.getLogin().getUri());
      Thread.sleep(waitingTimeMillis*2);

      login();
    }
  }

  public void loginIfPresent() {
    if(isLoginScreenPresent())
      login();
  }

  private void login() {
    log.info("start {}", this.getClass().getSimpleName());

    WebDriverWait wait = driverProviderService.getWebDriverWait();
    Configuration properties = propertiesReader.get().getConfiguration();

    String username = properties.getLogin().getUsername();
    String password = properties.getLogin().getPassword();

    WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("j_username")));
    usernameField.sendKeys(username);

    WebElement passwordField = driverProviderService.getChromeDriver().findElement(By.id("j_password"));
    passwordField.sendKeys(password);

    WebElement signInButton = driverProviderService.getChromeDriver().findElement(By.cssSelector("button.jenkins-button.jenkins-button--primary"));
    signInButton.click();
  }

  public boolean isLoginScreenPresent() {
    WebDriverWait wait = driverProviderService.getWebDriverWait();
    try {
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Sign in to Jenkins']")));
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
