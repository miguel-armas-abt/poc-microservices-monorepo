package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.connector.menuv1.retrofit;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.connector.MenuApiConnector;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionSaveRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionUpdateRequestDto;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MenuV1ApiConnectorRetrofitImpl implements MenuApiConnector {

  private final MenuV1Api menuV1Api;

  @Override
  public Observable<MenuOptionDto> findByCategory(String category) {
    return menuV1Api.findByCategory(category).flatMapObservable(Observable::fromIterable);
  }

  @Override
  public Maybe<MenuOptionDto> findByProductCode(String productCode) {
    return menuV1Api.findByProductCode(productCode).toMaybe();
  }

  @Override
  public Completable save(MenuOptionSaveRequestDto menuOption) {
    return menuV1Api.save(menuOption).ignoreElement();
  }

  @Override
  public Completable update(String productCode, MenuOptionUpdateRequestDto menuOption) {
    return menuV1Api.update(productCode, menuOption).ignoreElement();
  }

  @Override
  public Completable delete(String productCode) {
    return menuV1Api.delete(productCode).ignoreElement();
  }

  @Override
  public boolean supports(Class<?> selectedClass) {
    return this.getClass().isAssignableFrom(selectedClass);
  }

}
