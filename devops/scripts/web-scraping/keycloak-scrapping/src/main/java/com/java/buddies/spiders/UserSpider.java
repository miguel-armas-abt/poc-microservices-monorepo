package com.java.buddies.spiders;

import com.java.buddies.config.ScrapingConfig;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UserSpider {

  public static void configUser() throws InterruptedException {
    Thread.sleep(500);
    ChromeDriver driver = ScrapingConfig.getDriver();
    createUser(driver);
    assignCredentials(driver);
  }

  private static void createUser(ChromeDriver driver) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));

    WebElement usersOption = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, '/realms/bbq-management/users')]")));
    usersOption.click();

    WebElement addUserButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("createUser")));
    addUserButton.click();

    WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("username")));
    usernameField.sendKeys("admin");

    WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='submit' and contains(text(),'Save')]")));
    saveButton.click();
  }

  private static void assignCredentials(ChromeDriver driver) {
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(2));

    WebElement credentialsTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, 'user-credentials')]")));
    credentialsTab.click();

    WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("newPas")));
    passwordField.sendKeys("admin");

    WebElement confirmPasswordField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("confirmPas")));
    confirmPasswordField.sendKeys("admin");

    WebElement temporarySwitch = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='temporaryPassword']")));
    Actions actions = new Actions(driver);
    actions.moveToElement(temporarySwitch).click().perform();

    WebElement setPasswordButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(),'Set Password')]")));
    setPasswordButton.click();

    WebElement confirmSetPasswordButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[contains(text(), 'Set password')]")));
    confirmSetPasswordButton.click();
  }
}
