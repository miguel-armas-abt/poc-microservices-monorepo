package com.java.buddies.spiders;

import com.java.buddies.config.ScrapingConfig;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ClientSpider {

  public static void configClient() throws InterruptedException {
    Thread.sleep(500);
    ChromeDriver driver = ScrapingConfig.getDriver();

    createClient(driver);
    fillForm(driver);
  }

  private static void createClient(ChromeDriver driver) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

    WebElement clientsOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, '/realms/bbq-management/clients')]")));
    clientsOption.click();

    WebElement createClientButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("createClient")));
    createClientButton.click();

    WebElement clientIdField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("clientId")));
    clientIdField.sendKeys("front-bbq-app");

    WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Save')]")));
    saveButton.click();
  }

  private static void fillForm(ChromeDriver driver) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
    WebElement validRedirectUriField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("newRedirectUri")));
    validRedirectUriField.sendKeys("*");

    WebElement addRedirectUriButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='button' and contains(@class, 'btn-default')]//span[contains(@class, 'fa-plus')]")));
    addRedirectUriButton.click();

    WebElement finalSaveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Save')]")));
    finalSaveButton.click();
  }
}
