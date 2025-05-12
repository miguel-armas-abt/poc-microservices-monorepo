package com.demo.poc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.demo.poc.config.ComponentsConfig;
import com.demo.poc.service.ScrapingService;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    Injector injector = Guice.createInjector(new ComponentsConfig());
    ScrapingService scrapingService = injector.getInstance(ScrapingService.class);
    scrapingService.scrap();
  }

}