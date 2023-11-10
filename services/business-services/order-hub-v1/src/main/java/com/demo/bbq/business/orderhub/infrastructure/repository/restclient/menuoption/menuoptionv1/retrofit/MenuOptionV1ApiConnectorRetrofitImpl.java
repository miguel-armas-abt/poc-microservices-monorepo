package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menuoption.menuoptionv1.retrofit;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menuoption.MenuOptionApiConnector;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menuoption.dto.MenuOptionDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menuoption.dto.MenuOptionRequestDto;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class MenuOptionV1ApiConnectorRetrofitImpl implements MenuOptionApiConnector {

  private final MenuOptionV1Api menuOptionV1Api;

  @Override
  public Flux<MenuOptionDto> findByCategory(String category) {
    return RxJava2Adapter.observableToFlux(menuOptionV1Api
            .findByCategory(category)
            .flatMapObservable(Observable::fromIterable)
        , BackpressureStrategy.BUFFER);
  }

  @Override
  public Mono<MenuOptionDto> findById(Long id) {
    return RxJava2Adapter.singleToMono(menuOptionV1Api.findById(id));
  }

  @Override
  public Mono<Void> save(MenuOptionRequestDto menuOption) {
    return RxJava2Adapter.completableToMono(menuOptionV1Api.save(menuOption).ignoreElement());
  }

  @Override
  public Mono<Void> update(Long id, MenuOptionRequestDto menuOption) {
    return RxJava2Adapter.completableToMono(menuOptionV1Api.update(id, menuOption).ignoreElement());
  }

  @Override
  public Mono<Void> delete(Long id) {
    return RxJava2Adapter.completableToMono(menuOptionV1Api
        .delete(id)
        .ignoreElement());
  }

  @Override
  public boolean supports(Class<?> selectedClass) {
    return this.getClass().isAssignableFrom(selectedClass);
  }

}
