package com.java.buddies.spiders;

import com.java.buddies.config.ScrapingConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class LoginSpider {

  public static void login() throws InterruptedException {
    ChromeDriver driver = ScrapingConfig.getDriver();
    driver.get("http://localhost:8091/auth/");

    Thread.sleep(100);

    WebElement adminConsoleButton = driver.findElement(By.linkText("Administration Console"));
    adminConsoleButton.click();

    Thread.sleep(500);

    WebElement usernameField = driver.findElement(By.id("username"));
    usernameField.sendKeys("admin");

    WebElement passwordField = driver.findElement(By.id("password"));
    passwordField.sendKeys("admin");

    WebElement signInButton = driver.findElement(By.id("kc-login"));
    signInButton.click();
  }
}
