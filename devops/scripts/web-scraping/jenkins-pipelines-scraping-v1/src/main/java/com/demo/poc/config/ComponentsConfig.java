package com.demo.poc.config;

import com.demo.poc.spiders.*;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.demo.poc.properties.PropertiesReader;
import com.demo.poc.service.DriverProviderService;
import com.demo.poc.service.PipelineService;

public class ComponentsConfig extends AbstractModule {

  @Override
  protected void configure() {
    bind(PropertiesReader.class).toInstance(new PropertiesReader());
    bind(DriverProviderService.class).in(Singleton.class);
    bind(PipelineService.class);
    bind(LoginSpider.class);
    bind(PipelineSpider.class);
  }
}
