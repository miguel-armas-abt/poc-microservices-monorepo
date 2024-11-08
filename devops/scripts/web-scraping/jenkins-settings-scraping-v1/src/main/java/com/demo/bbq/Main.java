package com.demo.bbq;

import com.demo.bbq.service.SettingService;
import com.demo.bbq.spiders.LoginSpider;
import com.demo.bbq.spiders.ManageJenkinsSpider;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.demo.bbq.config.ComponentsConfig;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    Injector injector = Guice.createInjector(new ComponentsConfig());

    SettingService service = injector.getInstance(SettingService.class);
    service.scrapSettings();

//    LoginSpider loginSpider = injector.getInstance(LoginSpider.class);
//    loginSpider.loginIfNotPresent();
//
//    ManageJenkinsSpider manageJenkinsSpider = injector.getInstance(ManageJenkinsSpider.class);
//    manageJenkinsSpider.searchAndInstallK8sPlugin();
  }

}