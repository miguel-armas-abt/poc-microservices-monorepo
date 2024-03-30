package com.demo.bbq.business.orderhub.domain.repository.menu;

import com.demo.bbq.business.orderhub.application.helper.serviceselector.ServiceSelectorHelper;
import com.demo.bbq.business.orderhub.domain.repository.menu.properties.MenuProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuRepositoryHelper {

  private final ServiceSelectorHelper<MenuRepository> serviceSelectorHelper;
  private final MenuProperties menuProperties;

  public MenuRepository getService() {
    return serviceSelectorHelper.getService(menuProperties.getSelectorClass());
  }

}
