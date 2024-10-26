package com.demo.bbq.spiders;

import com.google.inject.Inject;
import com.demo.bbq.service.DriverProviderService;
import com.demo.bbq.properties.PropertiesReader;
import com.demo.bbq.properties.configuration.Configuration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class RealmCreatorSpider {

  @Inject
  private final PropertiesReader propertiesReader;

  @Inject
  private final DriverProviderService driverProviderService;

  public void createRealm() throws InterruptedException {
    log.info("start {}", this.getClass().getSimpleName());
    Thread.sleep(propertiesReader.get().getConfiguration().getWaitingTimeMillis());
    ChromeDriver driver = driverProviderService.getChromeDriver();
    WebDriverWait wait = driverProviderService.getWebDriverWait();
    Configuration properties = propertiesReader.get().getConfiguration();

    WebElement masterElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(), 'Master')]")));
    Actions actions = new Actions(driver);
    actions.moveToElement(masterElement).perform();
    WebElement addRealmButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(), 'Add realm')]")));
    addRealmButton.click();

    WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
    nameField.sendKeys(properties.getRealm().getName());

    WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Create')]")));
    createButton.click();
  }
}
