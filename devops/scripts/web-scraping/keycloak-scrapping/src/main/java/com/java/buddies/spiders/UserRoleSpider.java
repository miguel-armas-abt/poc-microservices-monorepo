package com.java.buddies.spiders;

import com.java.buddies.config.ScrapingConfig;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UserRoleSpider {

  public static void configRoleMappings() throws InterruptedException {
    Thread.sleep(500);
    ChromeDriver driver = ScrapingConfig.getDriver();
    findUser(driver);
    assignRole(driver);
  }

  private static void findUser(ChromeDriver driver) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

    WebElement rolesOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, '/realms/bbq-management/users')]")));
    rolesOption.click();

    WebElement viewAllUsersButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("viewAllUsers")));
    viewAllUsersButton.click();

    WebElement searchField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//input[@placeholder='Search...']")));
    searchField.sendKeys("admin");

    WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("userSearch")));
    searchButton.click();

    wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("user-table")));

    WebElement userLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//tr[td[text()='admin']]/td[1]/a")));
    userLink.click();
  }

  private static void assignRole(ChromeDriver driver) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));
    WebElement roleMappingsTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, 'role-mappings')]")));
    roleMappingsTab.click();

    WebElement availableRoles = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//option[@title='partners']")));
    availableRoles.click();

    WebElement addSelectedButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Add selected')]")));
    addSelectedButton.click();
  }
}
