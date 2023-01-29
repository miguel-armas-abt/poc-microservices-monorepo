package com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoptionv2;

import com.demo.bbq.business.diningroomorder.domain.exception.DiningRoomOrderException;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.MenuOptionStrategyInjector;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.dto.MenuOptionDto;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.dto.MenuOptionRequestDto;
import com.demo.bbq.support.reactive.httpclient.HttpStreamingTransformer;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Getter
@Slf4j
@NoArgsConstructor
public enum MenuOptionV2CallStrategy {

  RETROFIT{
    @Override
    public Flux<MenuOptionDto> findByCategory(MenuOptionStrategyInjector injector, String category) {
      return RxJava2Adapter.observableToFlux(injector.getMenuOptionV2RetrofitApi()
              .findByCategory(category)
              .compose(HttpStreamingTransformer.of(MenuOptionDto.class))
          , BackpressureStrategy.BUFFER);
    }

    @Override
    public Mono<MenuOptionDto> findById(MenuOptionStrategyInjector injector, Long id) {
      return RxJava2Adapter.singleToMono(injector.getMenuOptionV2RetrofitApi().findById(id));
    }

    @Override
    public Mono<Void> save(MenuOptionStrategyInjector injector, MenuOptionRequestDto menuOption) {
      return RxJava2Adapter.completableToMono(injector.getMenuOptionV2RetrofitApi().save(menuOption).ignoreElement());
    }

    @Override
    public Mono<Void> update(MenuOptionStrategyInjector injector, Long id, MenuOptionRequestDto menuOption) {
      return RxJava2Adapter.completableToMono(injector.getMenuOptionV2RetrofitApi().update(id, menuOption).ignoreElement());
    }

    @Override
    public Mono<Void> delete(MenuOptionStrategyInjector injector, Long id) {
      return RxJava2Adapter.completableToMono(injector.getMenuOptionV2RetrofitApi()
          .delete(id)
          .ignoreElement()
          .onErrorResumeNext(throwable -> Optional.ofNullable(((Exception) throwable).getMessage())
                .filter(error -> error.equals("HTTP 400 Bad Request"))
                .map(error -> Completable.error(DiningRoomOrderException.ERROR1001.buildCustomException()))
                .orElse(Completable.complete())));
    }
  };

  public abstract Flux<MenuOptionDto> findByCategory(MenuOptionStrategyInjector injector, String category);
  public abstract Mono<MenuOptionDto> findById(MenuOptionStrategyInjector injector, Long id);
  public abstract Mono<Void> save(MenuOptionStrategyInjector injector, MenuOptionRequestDto menuOption);
  public abstract Mono<Void> update(MenuOptionStrategyInjector injector, Long id, MenuOptionRequestDto menuOption);
  public abstract Mono<Void> delete(MenuOptionStrategyInjector injector, Long id);
}
