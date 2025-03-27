package com.demo.poc;

import com.demo.poc.service.PipelineService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.demo.poc.config.ComponentsConfig;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    Injector injector = Guice.createInjector(new ComponentsConfig());

    PipelineService service = injector.getInstance(PipelineService.class);
    service.scrapSettings();

//    LoginSpider loginSpider = injector.getInstance(LoginSpider.class);
//    loginSpider.loginIfNotPresent();

//    ManageJenkinsSpider manageJenkinsSpider = injector.getInstance(ManageJenkinsSpider.class);
//    manageJenkinsSpider.searchAndInstallK8sPlugin();
  }

}