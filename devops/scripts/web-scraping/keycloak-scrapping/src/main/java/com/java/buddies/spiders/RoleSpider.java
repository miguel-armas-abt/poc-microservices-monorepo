package com.java.buddies.spiders;

import com.java.buddies.config.ScrapingConfig;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class RoleSpider {

  public static void configRole() throws InterruptedException {
    Thread.sleep(500);
    ChromeDriver driver = ScrapingConfig.getDriver();
    createRole(driver);
  }

  private static void createRole(ChromeDriver driver) throws InterruptedException {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));

    WebElement rolesOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, '/realms/bbq-management/roles')]")));
    rolesOption.click();

    WebElement addRoleButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("createRole")));
    addRoleButton.click();

    WebElement roleNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
    roleNameField.sendKeys("partners");

    WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Save')]")));
    saveButton.click();
  }
}
