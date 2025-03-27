package com.demo.poc.spiders;

import com.demo.poc.properties.PropertiesReader;
import com.demo.poc.properties.configuration.Credentials;
import com.demo.poc.service.DriverProviderService;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ManageCredentialsSpider {

  private final PropertiesReader propertiesReader;
  private final DriverProviderService driverProviderService;
  private final JenkinsManagerSpider jenkinsManagerSpider;

  public void addNewCredential() throws InterruptedException {
    goToGlobalCredentials();

    WebDriverWait wait = driverProviderService.getWebDriverWait();
    Credentials credentials = propertiesReader.get().getConfiguration().getK8s().getCredentials();

    WebElement kindDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("select.jenkins-select__input")));
    Select select = new Select(kindDropdown);
    select.selectByVisibleText(credentials.getKind());

    WebElement secretField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("_.secret")));
    secretField.sendKeys(credentials.getK8sPassword());

    WebElement idField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[checkurl='/manage/descriptorByName/org.jenkinsci.plugins.plaincredentials.impl.StringCredentialsImpl/checkId']")));
    idField.sendKeys(credentials.getId());

    WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.jenkins-submit-button")));
    createButton.click();

    WebElement dashboardLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.model-link[href='/']")));
    dashboardLink.click();
  }

  private void goToGlobalCredentials() throws InterruptedException {
    goToCredentials();

    WebDriverWait wait = driverProviderService.getWebDriverWait();
    WebElement globalLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/manage/credentials/store/system/domain/_/']")));
    globalLink.click();

    WebElement addCredentialsLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='/manage/credentials/store/system/domain/_/newCredentials']")));
    addCredentialsLink.click();
  }

  public void goToCredentials() throws InterruptedException {
    jenkinsManagerSpider.goToManageJenkins();

    WebDriverWait wait = driverProviderService.getWebDriverWait();
    WebElement credentialsLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='credentials']")));
    credentialsLink.click();
  }

}
