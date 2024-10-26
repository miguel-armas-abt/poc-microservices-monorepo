package com.java.buddies.config;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.java.buddies.properties.PropertiesReader;
import com.java.buddies.service.DriverProviderService;
import com.java.buddies.service.ScrapingService;
import com.java.buddies.spiders.*;

public class DependencyConfig extends AbstractModule {

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
