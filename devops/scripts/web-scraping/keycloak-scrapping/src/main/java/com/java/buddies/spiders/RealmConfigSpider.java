package com.java.buddies.spiders;

import com.java.buddies.config.ScrapingConfig;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RealmConfigSpider {

  public static void configRealm() throws InterruptedException {
    Thread.sleep(500);
    ChromeDriver driver = ScrapingConfig.getDriver();
    getRS256Key(driver);
    updateAccessTokenLifespan(driver);
  }

  private static void getRS256Key(ChromeDriver driver) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
    WebElement keysTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Keys')]")));
    keysTab.click();

    WebElement rs256Row = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//td[contains(text(), 'RS256')]/following-sibling::td[2]")));

    String kidValue = rs256Row.getText();
    System.out.println("El valor de Kid para RS256 es: " + kidValue);
  }

  private static void updateAccessTokenLifespan(ChromeDriver driver) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
    WebElement keysTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(text(), 'Tokens')]")));
    keysTab.click();

    WebElement accessTokenLifespanInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("accessTokenLifespan")));
    accessTokenLifespanInput.clear();
    accessTokenLifespanInput.sendKeys("30");

    WebElement accessTokenLifespanUnit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("accessTokenLifespanUnit")));
    Select selectUnit = new Select(accessTokenLifespanUnit);
    selectUnit.selectByVisibleText("Minutes");

    WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@kc-save]")));
    saveButton.click();
  }
}
