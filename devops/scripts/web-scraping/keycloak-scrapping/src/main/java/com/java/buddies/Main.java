package com.java.buddies;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.java.buddies.config.DependencyConfig;
import com.java.buddies.service.ScrapingService;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    Injector injector = Guice.createInjector(new DependencyConfig());
    ScrapingService scrapingService = injector.getInstance(ScrapingService.class);
    scrapingService.scrap();
  }

}