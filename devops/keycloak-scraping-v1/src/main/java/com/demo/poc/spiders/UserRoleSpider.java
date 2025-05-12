package com.demo.poc.spiders;

import com.google.inject.Inject;
import com.demo.poc.service.DriverProviderService;
import com.demo.poc.properties.PropertiesReader;
import com.demo.poc.properties.configuration.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserRoleSpider {

  private final PropertiesReader propertiesReader;
  private final DriverProviderService driverProviderService;

  public void configRoleMappings() throws InterruptedException {
    log.info("start {}", this.getClass().getSimpleName());
    Thread.sleep(propertiesReader.get().getConfiguration().getWaitingTimeMillis());
    findUser();
    assignRole();
  }

  private void findUser() {
    WebDriverWait wait = driverProviderService.getWebDriverWait();
    Configuration properties = propertiesReader.get().getConfiguration();

    WebElement rolesOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, '/realms/poc-management/users')]")));
    rolesOption.click();

    WebElement viewAllUsersButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("viewAllUsers")));
    viewAllUsersButton.click();

    WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Search...']")));
    searchField.sendKeys(properties.getLogin().getUsername());

    WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("userSearch")));
    searchButton.click();

    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-table")));

    WebElement userLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[td[text()='admin']]/td[1]/a")));
    userLink.click();
  }

  private void assignRole() {
    WebDriverWait wait = driverProviderService.getWebDriverWait();
    WebElement roleMappingsTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, 'role-mappings')]")));
    roleMappingsTab.click();

    WebElement availableRoles = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//option[@title='partners']")));
    availableRoles.click();

    WebElement addSelectedButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Add selected')]")));
    addSelectedButton.click();
  }
}
