package com.java.buddies.spiders;

import com.google.inject.Inject;
import com.java.buddies.service.DriverProviderService;
import com.java.buddies.properties.PropertiesReader;
import com.java.buddies.properties.configuration.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class RoleSpider {

  @Inject
  private final PropertiesReader propertiesReader;

  @Inject
  private final DriverProviderService driverProviderService;

  public void configRole() throws InterruptedException {
    log.info("start {}", this.getClass().getSimpleName());
    Thread.sleep(propertiesReader.get().getConfiguration().getWaitingTimeMillis());
    createRole();
  }

  private void createRole() {
    WebDriverWait wait = driverProviderService.getWebDriverWait();
    Configuration properties = propertiesReader.get().getConfiguration();

    WebElement rolesOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, '/realms/bbq-management/roles')]")));
    rolesOption.click();

    WebElement addRoleButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("createRole")));
    addRoleButton.click();

    WebElement roleNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
    roleNameField.sendKeys(properties.getRole().getName());

    WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Save')]")));
    saveButton.click();
  }
}
