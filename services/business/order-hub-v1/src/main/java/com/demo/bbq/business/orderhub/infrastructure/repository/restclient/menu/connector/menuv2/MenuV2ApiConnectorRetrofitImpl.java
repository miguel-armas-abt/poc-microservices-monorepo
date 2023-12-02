package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.connector.menuv2;

import java.util.Optional;
import com.demo.bbq.business.orderhub.domain.exception.OrderHubException;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.connector.MenuApiConnector;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionSaveRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionUpdateRequestDto;
import com.demo.bbq.support.httpclient.retrofit.reactive.HttpStreamingTransformer;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MenuV2ApiConnectorRetrofitImpl implements MenuApiConnector {

  private final MenuV2Api menuV2Api;

  @Override
  public Observable<MenuOptionDto> findByCategory(String category) {
    return menuV2Api.findByCategory(category).compose(HttpStreamingTransformer.of(MenuOptionDto.class));
  }

  @Override
  public Maybe<MenuOptionDto> findByProductCode(String productCode) {
    return menuV2Api.findByProductCode(productCode).toMaybe();
  }

  @Override
  public Completable save(MenuOptionSaveRequestDto menuOption) {
    return menuV2Api.save(menuOption).ignoreElement();
  }

  @Override
  public Completable update(String productCode, MenuOptionUpdateRequestDto menuOption) {
    return menuV2Api.update(productCode, menuOption).ignoreElement();
  }

  @Override
  public Completable delete(String productCode) {
    return menuV2Api
        .delete(productCode)
        .ignoreElement()
        .onErrorResumeNext(throwable -> Optional.ofNullable(((Exception) throwable).getMessage())
            .filter(error -> error.equals("HTTP 400 Bad Request"))
            .map(error -> Completable.error(OrderHubException.ERROR0000.buildException()))
            .orElse(Completable.complete()));
  }

  @Override
  public boolean supports(Class<?> selectedClass) {
    return this.getClass().isAssignableFrom(selectedClass);
  }
}
