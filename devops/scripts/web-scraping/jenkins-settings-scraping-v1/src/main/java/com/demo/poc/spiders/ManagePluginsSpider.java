package com.demo.poc.spiders;

import com.demo.poc.properties.PropertiesReader;
import com.demo.poc.service.DriverProviderService;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ManagePluginsSpider {

  private final PropertiesReader propertiesReader;
  private final DriverProviderService driverProviderService;
  private final JenkinsManagerSpider jenkinsManagerSpider;

  public void searchAndInstallK8sPlugin() throws InterruptedException {
    searchPlugin("Kubernetes", "kubernetes", "plugin.kubernetes.default");
    clickOnInstallPlugin();
    clickOnRestartJenkins();
  }

  private void clickOnRestartJenkins() throws InterruptedException {
    WebDriverWait wait = driverProviderService.getWebDriverWait();

    JavascriptExecutor jsExecutor = driverProviderService.getChromeDriver();
    WebElement restartLabel = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//label[contains(text(), 'Restart Jenkins when installation is complete and no jobs are running')]")));
    jsExecutor.executeScript("arguments[0].scrollIntoView(true);", restartLabel);

    restartLabel.click();

    Thread.sleep(propertiesReader.get().getConfiguration().getWaitingTimeAfterRestart());
  }

  private void clickOnInstallPlugin() {
    WebDriverWait wait = driverProviderService.getWebDriverWait();
    WebElement installButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("button-install")));
    installButton.click();
  }

  private void searchPlugin(String pluginName, String pluginId, String label) throws InterruptedException {
    goToAvailablePlugins();

    WebDriverWait wait = driverProviderService.getWebDriverWait();

    WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("filter-box")));
    searchBox.clear();
    searchBox.sendKeys(pluginName);

    String query = "//tr[@data-plugin-id='" + pluginId + "']//label[@for='" + label + "']";

    WebElement pluginLabel = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(query)));
    pluginLabel.click();
  }

  public void goToAvailablePlugins() throws InterruptedException {
    goToPlugins();

    WebDriverWait wait = driverProviderService.getWebDriverWait();
    WebElement availablePluginsLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a.task-link-no-confirm[href='/manage/pluginManager/available']")));
    availablePluginsLink.click();
  }

  public void goToPlugins() throws InterruptedException {
    jenkinsManagerSpider.goToManageJenkins();

    WebDriverWait wait = driverProviderService.getWebDriverWait();
    WebElement pluginsLink = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("a[href='pluginManager']")));
    pluginsLink.click();
  }

}
