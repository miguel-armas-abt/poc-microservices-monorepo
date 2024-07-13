package com.demo.bbq.entrypoint.menu.repository;

import com.demo.bbq.commons.toolkit.serviceselector.ServiceSelectorHelper;
import com.demo.bbq.commons.properties.ApplicationProperties;
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
