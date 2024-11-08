package com.demo.bbq.service;

import com.demo.bbq.spiders.InstallPluginSpider;
import com.demo.bbq.spiders.ManageJenkinsSpider;
import com.demo.bbq.spiders.UnlockSpider;
import com.google.inject.Inject;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class SettingService {

  private final UnlockSpider unlockSpider;
  private final InstallPluginSpider installPluginSpider;
  private final ManageJenkinsSpider manageJenkinsSpider;

  public void scrapSettings() throws InterruptedException {
    unlockSpider.unlock();
    installPluginSpider.customizeJenkins();
    manageJenkinsSpider.searchAndInstallK8sPlugin();
  }
}
