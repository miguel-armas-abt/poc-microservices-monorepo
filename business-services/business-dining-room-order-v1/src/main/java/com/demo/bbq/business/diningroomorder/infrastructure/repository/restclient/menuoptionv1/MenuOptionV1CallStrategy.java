package com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoptionv1;

import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.MenuOptionStrategyInjector;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.dto.MenuOptionDto;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.dto.MenuOptionRequestDto;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Observable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Getter
@NoArgsConstructor
public enum MenuOptionV1CallStrategy {

  RETROFIT{
    @Override
    public Flux<MenuOptionDto> findByCategory(MenuOptionStrategyInjector injector, String category) {
      return RxJava2Adapter.observableToFlux(injector.getMenuOptionV1RetrofitApi()
              .findByCategory(category)
              .flatMapObservable(Observable::fromIterable)
          , BackpressureStrategy.BUFFER);
    }

    @Override
    public Mono<MenuOptionDto> findById(MenuOptionStrategyInjector injector, Long id) {
      return RxJava2Adapter.singleToMono(injector.getMenuOptionV1RetrofitApi().findById(id));
    }

    @Override
    public Mono<Void> save(MenuOptionStrategyInjector injector, MenuOptionRequestDto menuOption) {
      return RxJava2Adapter.completableToMono(injector.getMenuOptionV1RetrofitApi().save(menuOption).ignoreElement());
    }

    @Override
    public Mono<Void> update(MenuOptionStrategyInjector injector, Long id, MenuOptionRequestDto menuOption) {
      return RxJava2Adapter.completableToMono(injector.getMenuOptionV1RetrofitApi().update(id, menuOption).ignoreElement());
    }

    @Override
    public Mono<Void> delete(MenuOptionStrategyInjector injector, Long id) {
      return RxJava2Adapter.completableToMono(injector.getMenuOptionV1RetrofitApi()
          .delete(id)
          .ignoreElement());
    }
  },

  WEBCLIENT{
    @Override
    public Flux<MenuOptionDto> findByCategory(MenuOptionStrategyInjector injector, String category) {
      return injector.getMenuOptionV1WebClientApi().findByCategory(category);
    }

    @Override
    public Mono<MenuOptionDto> findById(MenuOptionStrategyInjector injector, Long id) {
      return injector.getMenuOptionV1WebClientApi().findById(id);
    }

    @Override
    public Mono<Void> save(MenuOptionStrategyInjector injector, MenuOptionRequestDto menuOption) {
      return injector.getMenuOptionV1WebClientApi().save(menuOption);
    }

    @Override
    public Mono<Void> update(MenuOptionStrategyInjector injector, Long id, MenuOptionRequestDto menuOption) {
      return injector.getMenuOptionV1WebClientApi().update(id, menuOption);
    }

    @Override
    public Mono<Void> delete(MenuOptionStrategyInjector injector, Long id) {
      return injector.getMenuOptionV1WebClientApi().delete(id);
    }
  };

  public abstract Flux<MenuOptionDto> findByCategory(MenuOptionStrategyInjector injector, String category);
  public abstract Mono<MenuOptionDto> findById(MenuOptionStrategyInjector injector, Long id);
  public abstract Mono<Void> save(MenuOptionStrategyInjector injector, MenuOptionRequestDto menuOption);
  public abstract Mono<Void> update(MenuOptionStrategyInjector injector, Long id, MenuOptionRequestDto menuOption);
  public abstract Mono<Void> delete(MenuOptionStrategyInjector injector, Long id);
}
