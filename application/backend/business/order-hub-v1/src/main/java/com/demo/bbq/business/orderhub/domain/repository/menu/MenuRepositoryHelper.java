package com.demo.bbq.business.orderhub.domain.repository.menu;

import com.demo.bbq.business.orderhub.application.helper.serviceselector.ServiceSelectorHelper;
import com.demo.bbq.business.orderhub.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.errors.exceptions.SystemException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuRepositoryHelper {

  private final ServiceSelectorHelper<MenuRepository> serviceSelectorHelper;
  private final ServiceConfigurationProperties properties;

  public MenuRepository getService() {
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
