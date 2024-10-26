package com.demo.bbq;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.demo.bbq.config.DependencyConfig;
import com.demo.bbq.service.ScrapingService;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    Injector injector = Guice.createInjector(new DependencyConfig());
    ScrapingService scrapingService = injector.getInstance(ScrapingService.class);
    scrapingService.scrap();
  }

}