package com.demo.poc.spiders;

import com.demo.poc.properties.PropertiesReader;
import com.demo.poc.properties.configuration.Configuration;
import com.demo.poc.service.DriverProviderService;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class SuggestedPluginSpider {

  private static final String INSTALL_PLUGINS_BTN = "a.btn.btn-primary.btn-lg.btn-huge.install-recommended";
  private static final String IFRAME_SETUP_FIRST_USER = "setup-first-user";
  private static final String IFRAME_CONFIGURE_INSTANCE = "setup-configure-instance";

  private final PropertiesReader propertiesReader;
  private final DriverProviderService driverProviderService;
  private final LoginSpider loginSpider;

  public void customizeJenkins() throws InterruptedException {
    log.info("start {}", this.getClass().getSimpleName());
    Thread.sleep(propertiesReader.get().getConfiguration().getWaitingTimeMillis());

    loginSpider.loginIfPresent();
    installPlugins();
    createAdminUser();
    uriConfig();
    startJenkins();
  }

  private void installPlugins() throws InterruptedException {
    if(isInstallPluginsScreenPresent()) {
      WebDriverWait wait = driverProviderService.getWebDriverWait();

      WebElement installButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(INSTALL_PLUGINS_BTN)));
      installButton.click();

      waitProgressBar();

    } else {
      log.warn("Plugins installation screen not detected.");
    }
  }

  private void waitProgressBar() throws InterruptedException {
    WebDriverWait wait = driverProviderService.getWebDriverWait();
    Configuration properties = propertiesReader.get().getConfiguration();

    WebElement progressBarContainer = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".progress")));
    int containerWidth = progressBarContainer.getSize().getWidth();

    WebElement progressBar = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".progress-bar")));
    boolean isProgressComplete = false;

    while (!isProgressComplete) {
      String widthValue = progressBar.getCssValue("width").replace("px", "");
      double progressWidth = Double.parseDouble(widthValue);
      double progressPercentage = (progressWidth / containerWidth) * 100;

      if (progressPercentage > 99) {
        isProgressComplete = true;
        Thread.sleep(properties.getWaitingTimeAfterSuggestedPlugins());
      } else {
        Thread.sleep(properties.getWaitingTimeMillis());
      }
    }
  }

  private void createAdminUser() {
    if(isCreateAdminUserScreenPresent()) {
      WebDriverWait wait = driverProviderService.getWebDriverWait();
      Configuration properties = propertiesReader.get().getConfiguration();

      String username = properties.getLogin().getUsername();
      String password = properties.getLogin().getPassword();
      String email = properties.getLogin().getEmail();

      //go to iframe
      wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(IFRAME_SETUP_FIRST_USER)));

      WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
      usernameField.sendKeys(username);

      WebElement passwordField = driverProviderService.getChromeDriver().findElement(By.name("password1"));
      passwordField.sendKeys(password);

      WebElement confirmPasswordField = driverProviderService.getChromeDriver().findElement(By.name("password2"));
      confirmPasswordField.sendKeys(password);

      WebElement fullNameField = driverProviderService.getChromeDriver().findElement(By.name("fullname"));
      fullNameField.sendKeys(username);

      WebElement emailField = driverProviderService.getChromeDriver().findElement(By.name("email"));
      emailField.sendKeys(email);

      //exit iframe
      driverProviderService.getChromeDriver().switchTo().defaultContent();

      WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.btn-primary.save-first-user")));
      saveButton.click();

    } else {
      log.warn("Admin user creation screen not detected.");
    }
  }

  private void uriConfig() {
    if (isURIConfigScreenPresent()) {
      WebDriverWait wait = driverProviderService.getWebDriverWait();
      wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(IFRAME_CONFIGURE_INSTANCE)));

      //exit iframe
      driverProviderService.getChromeDriver().switchTo().defaultContent();

      WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.btn-primary.save-configure-instance")));
      saveButton.click();

    } else {
      log.warn("URI config screen not detected.");
    }
  }

  private void startJenkins() {
    if (isStartingScreenPresent()) {
      WebDriverWait wait = driverProviderService.getWebDriverWait();
      WebElement startButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.btn.btn-primary.install-done")));
      startButton.click();

    } else {
      log.warn("Starting Jenkins screen not detected.");
    }
  }

  private boolean isInstallPluginsScreenPresent() {
    WebDriverWait wait = driverProviderService.getWebDriverWait();
    try {
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(INSTALL_PLUGINS_BTN)));
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean isCreateAdminUserScreenPresent() {
    WebDriverWait wait = driverProviderService.getWebDriverWait();
    try {
      wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(IFRAME_SETUP_FIRST_USER)));
      //exit iframe
      driverProviderService.getChromeDriver().switchTo().defaultContent();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean isURIConfigScreenPresent() {
    WebDriverWait wait = driverProviderService.getWebDriverWait();
    try {
      wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.id(IFRAME_CONFIGURE_INSTANCE)));
      //exit iframe
      driverProviderService.getChromeDriver().switchTo().defaultContent();
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private boolean isStartingScreenPresent() {
    WebDriverWait wait = driverProviderService.getWebDriverWait();
    try {
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h1[text()='Jenkins is ready!']")));
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
