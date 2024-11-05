package com.demo.bbq.config;

import com.demo.bbq.spiders.*;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.demo.bbq.properties.PropertiesReader;
import com.demo.bbq.service.DriverProviderService;
import com.demo.bbq.service.ScrapingService;

public class ComponentsConfig extends AbstractModule {

  @Override
  protected void configure() {
    bind(PropertiesReader.class).toInstance(new PropertiesReader());
    bind(DriverProviderService.class).in(Singleton.class);
    bind(ClientSpider.class);
    bind(LoginSpider.class);
    bind(RealmConfigSpider.class);
    bind(RealmCreatorSpider.class);
    bind(RoleSpider.class);
    bind(UserRoleSpider.class);
    bind(UserSpider.class);
    bind(ScrapingService.class);
  }
}
