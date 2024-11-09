package com.demo.bbq.config;

import com.demo.bbq.spiders.*;
import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.demo.bbq.properties.PropertiesReader;
import com.demo.bbq.service.DriverProviderService;
import com.demo.bbq.service.SettingService;

public class ComponentsConfig extends AbstractModule {

  @Override
  protected void configure() {
    bind(PropertiesReader.class).toInstance(new PropertiesReader());
    bind(DriverProviderService.class).in(Singleton.class);
    bind(SettingService.class);
    bind(UnlockSpider.class);
    bind(SuggestedPluginSpider.class);
    bind(LoginSpider.class);
    bind(ManagePluginsSpider.class);
    bind(JenkinsManagerSpider.class);
    bind(ManageCloudSpider.class);
  }
}
