package com.demo.bbq.repository.menu;

import com.demo.bbq.application.helper.serviceselector.ServiceSelectorHelper;
import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.errors.exceptions.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuRepositoryStrategy {

  private final ServiceSelectorHelper<MenuRepositoryHelper> serviceSelectorHelper;
  private final ServiceConfigurationProperties properties;

  public MenuRepositoryHelper getService() {
    return serviceSelectorHelper.getService(getSelectorClass());
  }

  private Class<?> getSelectorClass() {
    try {
      String stringClass = properties.getVariables().get("menu-selector-class");
      return Class.forName(stringClass);
    } catch (ClassNotFoundException exception) {
      throw new SystemException("SectorClassNotFound");
    }
  }
}
