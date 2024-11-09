package com.demo.bbq.service;

import com.demo.bbq.spiders.*;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class SettingService {

  private final UnlockSpider unlockSpider;
  private final SuggestedPluginSpider suggestedPluginSpider;
  private final ManagePluginsSpider managePluginsSpider;
  private final LoginSpider loginSpider;
  private final ManageCredentialsSpider manageCredentialsSpider;
  private final ManageCloudSpider manageCloudSpider;

  public void scrapSettings() throws InterruptedException {
    unlockSpider.unlock();
    suggestedPluginSpider.customizeJenkins();
    managePluginsSpider.searchAndInstallK8sPlugin();
    loginSpider.loginIfNotPresent();
    manageCredentialsSpider.addNewCredential();
    manageCloudSpider.addK8sCloud();
  }
}
