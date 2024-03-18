package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.connector.MenuApiConnector;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionSaveRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionUpdateRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.properties.MenuOptionSelectorClassProperties;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuApi {

  private static final String API_CLIENT = "menu-option";
  private final List<MenuApiConnector> menuApiConnectorList;
  private final MenuOptionSelectorClassProperties properties;

  private MenuApiConnector getService(Class<?> selectorClass) {
    return menuApiConnectorList.stream()
        .filter(service -> service.supports(selectorClass))
        .findFirst()
        .orElseThrow(NullPointerException::new);
  }

  public Maybe<MenuOptionDto> findByProductCode(String productCode) {
    return getService(properties.getSelectorClass()).findByProductCode(productCode);
  }

  public Observable<MenuOptionDto> findByCategory(String category) {
    return getService(properties.getSelectorClass()).findByCategory(category);
  }

  public Completable save(MenuOptionSaveRequestDto menuOptionRequest) {
    return getService(properties.getSelectorClass()).save(menuOptionRequest);
  }

  public Completable update(String productCode, MenuOptionUpdateRequestDto menuOptionRequest) {
    return getService(properties.getSelectorClass()).update(productCode, menuOptionRequest);
  }

  public Completable delete(String productCode) {
    return getService(properties.getSelectorClass()).delete(productCode);
  }

}