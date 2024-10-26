package com.demo.bbq.spiders;

import com.google.inject.Inject;
import com.demo.bbq.service.DriverProviderService;
import com.demo.bbq.properties.PropertiesReader;
import com.demo.bbq.properties.configuration.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ClientSpider {

  @Inject
  private final PropertiesReader propertiesReader;

  @Inject
  private final DriverProviderService driverProviderService;

  public void configClient() throws InterruptedException {
    log.info("start {}", this.getClass().getSimpleName());
    Thread.sleep(propertiesReader.get().getConfiguration().getWaitingTimeMillis());
    createClient();
    fillForm();
  }

  private void createClient() {
    WebDriverWait wait = driverProviderService.getWebDriverWait();
    Configuration properties = propertiesReader.get().getConfiguration();

    WebElement clientsOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, '/realms/bbq-management/clients')]")));
    clientsOption.click();

    WebElement createClientButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("createClient")));
    createClientButton.click();

    WebElement clientIdField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("clientId")));
    clientIdField.sendKeys(properties.getClient().getName());

    WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Save')]")));
    saveButton.click();
  }

  private void fillForm() {
    WebDriverWait wait = driverProviderService.getWebDriverWait();
    Configuration properties = propertiesReader.get().getConfiguration();

    WebElement validRedirectUriField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("newRedirectUri")));
    validRedirectUriField.sendKeys(properties.getClient().getRedirectUriFields());

    WebElement addRedirectUriButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='button' and contains(@class, 'btn-default')]//span[contains(@class, 'fa-plus')]")));
    addRedirectUriButton.click();

    WebElement finalSaveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Save')]")));
    finalSaveButton.click();
  }
}
