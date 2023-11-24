package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.menuv2.retrofit;

import java.util.Optional;
import com.demo.bbq.business.orderhub.domain.exception.OrderHubException;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.MenuApiConnector;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionSaveRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionUpdateRequestDto;
import com.demo.bbq.support.httpclient.retrofit.reactive.HttpStreamingTransformer;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class MenuV2ApiConnectorRetrofitImpl implements MenuApiConnector {

  private final MenuV2Api menuV2Api;

  @Override
  public Flux<MenuOptionDto> findByCategory(String category) {
    return RxJava2Adapter.observableToFlux(menuV2Api
            .findByCategory(category)
            .compose(HttpStreamingTransformer.of(MenuOptionDto.class))
        , BackpressureStrategy.BUFFER);
  }

  @Override
  public Mono<MenuOptionDto> findByProductCode(String productCode) {
    return RxJava2Adapter.singleToMono(menuV2Api.findByProductCode(productCode));
  }

  @Override
  public Mono<Void> save(MenuOptionSaveRequestDto menuOption) {
    return RxJava2Adapter.completableToMono(menuV2Api.save(menuOption).ignoreElement());
  }

  @Override
  public Mono<Void> update(String productCode, MenuOptionUpdateRequestDto menuOption) {
    return RxJava2Adapter.completableToMono(menuV2Api.update(productCode, menuOption).ignoreElement());
  }

  @Override
  public Mono<Void> delete(String productCode) {
    return RxJava2Adapter.completableToMono(menuV2Api
        .delete(productCode)
        .ignoreElement()
        .onErrorResumeNext(throwable -> Optional.ofNullable(((Exception) throwable).getMessage())
            .filter(error -> error.equals("HTTP 400 Bad Request"))
            .map(error -> Completable.error(OrderHubException.ERROR0000.buildException()))
            .orElse(Completable.complete())));
  }

  @Override
  public boolean supports(Class<?> selectedClass) {
    return this.getClass().isAssignableFrom(selectedClass);
  }
}
