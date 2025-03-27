package com.demo.poc.spiders;

import com.demo.poc.properties.PropertiesReader;
import com.demo.poc.service.DriverProviderService;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class JenkinsManagerSpider {

  private final PropertiesReader propertiesReader;
  private final DriverProviderService driverProviderService;

  public void goToManageJenkins() throws InterruptedException {
    log.info("start {}", this.getClass().getSimpleName());
    Thread.sleep(propertiesReader.get().getConfiguration().getWaitingTimeMillis());

    WebDriverWait wait = driverProviderService.getWebDriverWait();

    WebElement dashboardLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.model-link")));
    dashboardLink.click();

    WebElement manageJenkinsLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.task-link-no-confirm[href='/manage']")));
    manageJenkinsLink.click();
  }
}
