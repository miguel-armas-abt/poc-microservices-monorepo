package com.demo.bbq.spiders;

import com.demo.bbq.properties.PropertiesReader;
import com.demo.bbq.properties.configuration.Pipeline;
import com.demo.bbq.service.DriverProviderService;
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
public class PipelineSpider {

  private final PropertiesReader propertiesReader;
  private final DriverProviderService driverProviderService;

  public void configureNewPipeline() throws InterruptedException {
    selectNewPipeline();

    WebDriverWait wait = driverProviderService.getWebDriverWait();
    Pipeline pipeline = propertiesReader.get().getConfiguration().getPipeline();

    WebElement githubProjectCheckboxLabel = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[text()='GitHub project']")));
    githubProjectCheckboxLabel.click();

    WebElement projectUrlInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("_.projectUrlStr")));
    projectUrlInput.sendKeys(pipeline.getGithubProject());

    WebElement pipelineSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pipeline")));
    driverProviderService.getChromeDriver().executeScript("arguments[0].scrollIntoView(true);", pipelineSection);

    WebElement definitionDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[contains(@class, 'jenkins-select__input') and contains(@class, 'dropdownList')]")));
    driverProviderService.getChromeDriver().executeScript("arguments[0].click();", definitionDropdown);

    WebElement optionPipelineScript = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[contains(@class, 'jenkins-select__input') and contains(@class, 'dropdownList')]/option[contains(text(), 'Pipeline script')]")));
    optionPipelineScript.click();
  }

  private void selectNewPipeline() throws InterruptedException {
    goToNewPipeline();

    WebDriverWait wait = driverProviderService.getWebDriverWait();
    String pipelineName = propertiesReader.get().getConfiguration().getPipeline().getName();

    WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
    nameInput.sendKeys(pipelineName + "8");

    WebElement pipelineOption = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("li.org_jenkinsci_plugins_workflow_job_WorkflowJob")));
    pipelineOption.click();

    WebElement okButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("ok-button")));
    okButton.click();
  }

  private void goToNewPipeline() throws InterruptedException {
    log.info("start {}", this.getClass().getSimpleName());
    Thread.sleep(propertiesReader.get().getConfiguration().getWaitingTimeMillis());

    WebDriverWait wait = driverProviderService.getWebDriverWait();

    WebElement homeLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("jenkins-home-link")));
    homeLink.click();

    WebElement newItemSpan = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("span.task-link-wrapper")));
    WebElement newItemLink = newItemSpan.findElement(By.xpath(".//a[contains(@href, '/view/all/newJob')]"));
    newItemLink.click();
  }
}
