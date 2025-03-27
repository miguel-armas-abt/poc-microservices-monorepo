package com.demo.poc.spiders;

import com.demo.poc.properties.PropertiesReader;
import com.demo.poc.properties.configuration.Pipeline;
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
public class PipelineSpider {

  private final PropertiesReader propertiesReader;
  private final DriverProviderService driverProviderService;

  private void setJenkinsFile(Pipeline pipeline) {
    WebDriverWait wait = driverProviderService.getWebDriverWait();

    WebElement scriptPathSection = wait.until(ExpectedConditions.visibilityOfElementLocated(
        By.xpath("//div[contains(@class, 'jenkins-form-item') and .//div[text()='Script Path']]")));
    driverProviderService.getChromeDriver().executeScript("arguments[0].scrollIntoView(true);", scriptPathSection);

    WebElement scriptPathInput = wait.until(ExpectedConditions.presenceOfElementLocated(
        By.xpath("//input[@name='_.scriptPath' and @type='text' and contains(@class, 'jenkins-input')]")));
    scriptPathInput.clear();
    scriptPathInput.sendKeys(pipeline.getJenkinsFilePath());
  }

  public void configureNewPipeline() throws InterruptedException {
    selectNewPipeline();

    WebDriverWait wait = driverProviderService.getWebDriverWait();
    Pipeline pipeline = propertiesReader.get().getConfiguration().getPipeline();

    setGitHubProject(pipeline);
    selectGitSCM();
    setRepository(pipeline);
    setJenkinsFile(pipeline);

    WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
        By.xpath("//div[@id='bottom-sticker']//button[@name='Submit' and contains(@class, 'jenkins-submit-button')]")));
    saveButton.click();
  }

  private void setGitHubProject(Pipeline pipeline) {
    WebDriverWait wait = driverProviderService.getWebDriverWait();
    WebElement githubProjectCheckboxLabel = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[text()='GitHub project']")));
    githubProjectCheckboxLabel.click();

    WebElement projectUrlInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("_.projectUrlStr")));
    projectUrlInput.sendKeys(pipeline.getGithubProject());
  }

  private void selectGitSCM() {
    WebDriverWait wait = driverProviderService.getWebDriverWait();

    WebElement pipelineSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pipeline")));
    driverProviderService.getChromeDriver().executeScript("arguments[0].scrollIntoView(true);", pipelineSection);

    WebElement definitionDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[contains(@class, 'jenkins-select__input') and contains(@class, 'dropdownList')]")));
    driverProviderService.getChromeDriver().executeScript("arguments[0].click();", definitionDropdown);

    WebElement optionPipelineScript = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[contains(@class, 'jenkins-select__input') and contains(@class, 'dropdownList')]/option[contains(text(), 'Pipeline script from SCM')]")));
    optionPipelineScript.click();

    WebElement scmDropdown = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//select[contains(@class, 'jenkins-select__input') and contains(@class, 'dropdownList')]")));
    driverProviderService.getChromeDriver().executeScript("arguments[0].click();", scmDropdown);

    WebElement optionGit = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//select[contains(@class, 'jenkins-select__input') and contains(@class, 'dropdownList')]/option[contains(text(), 'Git')]")));
    optionGit.click();
  }

  private void setRepository(Pipeline pipeline) {
    WebDriverWait wait = driverProviderService.getWebDriverWait();

    WebElement repositoryUrlInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("_.url")));
    repositoryUrlInput.sendKeys(pipeline.getRepositoryUrl());

    WebElement branchesToBuildSection = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(@class, 'jenkins-form-item') and .//div[text()='Branches to build']]")));
    driverProviderService.getChromeDriver().executeScript("arguments[0].scrollIntoView(true);", branchesToBuildSection);

    WebElement branchSpecifierInput = wait.until(ExpectedConditions.presenceOfElementLocated(
        By.xpath("//div[@name='branches']//input[@name='_.name' and contains(@class, 'jenkins-input')]")));

    branchSpecifierInput.clear();
    branchSpecifierInput.sendKeys(pipeline.getBranch());
  }

  private void selectNewPipeline() throws InterruptedException {
    goToNewPipeline();

    WebDriverWait wait = driverProviderService.getWebDriverWait();
    String pipelineName = propertiesReader.get().getConfiguration().getPipeline().getName();

    WebElement nameInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("name")));
    nameInput.sendKeys(pipelineName);

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
