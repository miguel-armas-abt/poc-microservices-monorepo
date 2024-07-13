package com.demo.bbq.repository.menu;

import com.demo.bbq.application.helper.serviceselector.ServiceSelectorHelper;
import com.demo.bbq.config.properties.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuRepositoryStrategy {

  private final ServiceSelectorHelper<MenuRepository> serviceSelectorHelper;
  private final ApplicationProperties properties;

  public MenuRepository getService() {
    return serviceSelectorHelper.getService(properties.getMenuInfo().getSelectorClass());
  }

}
