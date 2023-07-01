package com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoption.menuoptionv2.retrofit;

import java.util.Optional;
import com.demo.bbq.business.diningroomorder.domain.exception.DiningRoomOrderException;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoption.MenuOptionApiConnector;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoption.dto.MenuOptionDto;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoption.dto.MenuOptionRequestDto;
import com.demo.bbq.support.reactive.httpclient.HttpStreamingTransformer;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class MenuOptionV2ApiConnectorRetrofitImpl implements MenuOptionApiConnector {

  private final MenuOptionV2Api menuOptionV2Api;

  @Override
  public Flux<MenuOptionDto> findByCategory(String category) {
    return RxJava2Adapter.observableToFlux(menuOptionV2Api
            .findByCategory(category)
            .compose(HttpStreamingTransformer.of(MenuOptionDto.class))
        , BackpressureStrategy.BUFFER);
  }

  @Override
  public Mono<MenuOptionDto> findById(Long id) {
    return RxJava2Adapter.singleToMono(menuOptionV2Api.findById(id));
  }

  @Override
  public Mono<Void> save(MenuOptionRequestDto menuOption) {
    return RxJava2Adapter.completableToMono(menuOptionV2Api.save(menuOption).ignoreElement());
  }

  @Override
  public Mono<Void> update(Long id, MenuOptionRequestDto menuOption) {
    return RxJava2Adapter.completableToMono(menuOptionV2Api.update(id, menuOption).ignoreElement());
  }

  @Override
  public Mono<Void> delete(Long id) {
    return RxJava2Adapter.completableToMono(menuOptionV2Api
        .delete(id)
        .ignoreElement()
        .onErrorResumeNext(throwable -> Optional.ofNullable(((Exception) throwable).getMessage())
            .filter(error -> error.equals("HTTP 400 Bad Request"))
            .map(error -> Completable.error(DiningRoomOrderException.ERROR0001.buildException()))
            .orElse(Completable.complete())));
  }

  @Override
  public boolean supports(Class<?> selectedClass) {
    return this.getClass().isAssignableFrom(selectedClass);
  }
}
