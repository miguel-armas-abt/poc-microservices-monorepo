package com.demo.poc;

import com.demo.poc.service.SettingService;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.demo.poc.config.ComponentsConfig;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    Injector injector = Guice.createInjector(new ComponentsConfig());

    SettingService service = injector.getInstance(SettingService.class);
    service.scrapSettings();
  }

}