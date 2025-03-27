package com.demo.poc.spiders;

import com.demo.poc.properties.PropertiesReader;
import com.demo.poc.properties.configuration.Cloud;
import com.demo.poc.properties.configuration.K8s;
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
public class ManageCloudSpider {

  private final PropertiesReader propertiesReader;
  private final DriverProviderService driverProviderService;
  private final JenkinsManagerSpider jenkinsManagerSpider;

  public void addK8sCloud() throws InterruptedException {
    fillK8sCloudName();

    WebDriverWait wait = driverProviderService.getWebDriverWait();
    K8s k8s = propertiesReader.get().getConfiguration().getK8s();
    Cloud cloud = k8s.getCloud();

    WebElement kubernetesUrlField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("input[checkurl='/manage/descriptorByName/org.csanchez.jenkins.plugins.kubernetes.KubernetesCloud/checkServerUrl']")));
    kubernetesUrlField.sendKeys(cloud.getForwardedServerUrl());

    WebElement serverCertificateField = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("textarea[checkurl='/manage/descriptorByName/org.csanchez.jenkins.plugins.kubernetes.KubernetesCloud/checkServerCertificate']")));
    serverCertificateField.sendKeys(cloud.getCertificate());

    WebElement disableHttpsCheckLabel = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[span[text()='Disable https certificate check']]")));
    disableHttpsCheckLabel.click();

    WebElement credentialsDropdown = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("select[name='_.credentialsId']")));
    Select select = new Select(credentialsDropdown);
    select.selectByValue(k8s.getCredentials().getId());

    WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.jenkins-submit-button.jenkins-button--primary")));
    saveButton.click();
  }

  private void fillK8sCloudName() throws InterruptedException {
    addNewCloud();

    WebDriverWait wait = driverProviderService.getWebDriverWait();
    Cloud cloud = propertiesReader.get().getConfiguration().getK8s().getCloud();

    WebElement cloudNameField = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
    cloudNameField.sendKeys(cloud.getName());

    WebElement kubernetesLabel = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("label[for='org.csanchez.jenkins.plugins.kubernetes.KubernetesCloud']")));
    kubernetesLabel.click();

    WebElement createButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("button.jenkins-button")));
    createButton.click();
  }

  private void addNewCloud() throws InterruptedException {
    goToClouds();

    WebDriverWait wait = driverProviderService.getWebDriverWait();

    WebElement newCloudButton = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.content-block__link[href='new']")));
    newCloudButton.click();
  }

  private void goToClouds() throws InterruptedException {
    jenkinsManagerSpider.goToManageJenkins();

    WebDriverWait wait = driverProviderService.getWebDriverWait();
    WebElement credentialsLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='cloud']")));
    credentialsLink.click();
  }

}
