package com.java.buddies.spiders;

import com.java.buddies.config.ScrapingConfig;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RealmCreatorSpider {

  public static void createRealm() throws InterruptedException {
    Thread.sleep(500);
    ChromeDriver driver = ScrapingConfig.getDriver();
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

    WebElement masterElement = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[contains(text(), 'Master')]")));
    Actions actions = new Actions(driver);
    actions.moveToElement(masterElement).perform();
    WebElement addRealmButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(text(), 'Add realm')]")));
    addRealmButton.click();

    WebElement nameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
    nameField.sendKeys("bbq-management");

    WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Create')]")));
    createButton.click();
  }
}
