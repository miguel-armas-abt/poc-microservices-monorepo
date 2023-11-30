package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.menuv1.retrofit;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.MenuApiConnector;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionSaveRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionUpdateRequestDto;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class MenuV1ApiConnectorRetrofitImpl implements MenuApiConnector {

  private final MenuV1Api menuV1Api;

  @Override
  public Flux<MenuOptionDto> findByCategory(String category) {
    return RxJava2Adapter.observableToFlux(menuV1Api
            .findByCategory(category)
            .flatMapObservable(Observable::fromIterable)
        , BackpressureStrategy.BUFFER);
  }

  @Override
  public Mono<MenuOptionDto> findByProductCode(String productCode) {
    return RxJava2Adapter.singleToMono(menuV1Api.findByProductCode(productCode));
  }

  @Override
  public Mono<Void> save(MenuOptionSaveRequestDto menuOption) {
    return RxJava2Adapter.completableToMono(menuV1Api.save(menuOption).ignoreElement());
  }

  @Override
  public Mono<Void> update(String productCode, MenuOptionUpdateRequestDto menuOption) {
    return RxJava2Adapter.completableToMono(menuV1Api.update(productCode, menuOption).ignoreElement());
  }

  @Override
  public Mono<Void> delete(String productCode) {
    return RxJava2Adapter.completableToMono(menuV1Api
        .delete(productCode)
        .ignoreElement());
  }

  @Override
  public boolean supports(Class<?> selectedClass) {
    return this.getClass().isAssignableFrom(selectedClass);
  }

}
