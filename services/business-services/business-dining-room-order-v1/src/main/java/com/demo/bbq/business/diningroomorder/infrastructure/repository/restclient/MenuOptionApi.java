package com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient;

import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.dto.MenuOptionDto;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.dto.MenuOptionRequestDto;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoptionv1.MenuOptionV1CallStrategy;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoptionv2.MenuOptionV2CallStrategy;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class MenuOptionApi {

  private static final String API_CLIENT = "menu-option-v2";
  private final MenuOptionStrategyInjector injector;

  @CircuitBreaker(name = API_CLIENT, fallbackMethod = "alternativeFindById")
  public Mono<MenuOptionDto> findById(Long id) {
    return MenuOptionV2CallStrategy.RETROFIT.findById(injector, id);
  }

  @CircuitBreaker(name = API_CLIENT, fallbackMethod = "alternativeFindByCategory")
  public Flux<MenuOptionDto> findByCategory(String category) {
    return MenuOptionV2CallStrategy.RETROFIT.findByCategory(injector, category);
  }

  @CircuitBreaker(name = API_CLIENT, fallbackMethod = "alternativeSave")
  public Mono<Void> save(MenuOptionRequestDto menuOptionRequest) {
    return MenuOptionV2CallStrategy.RETROFIT.save(injector, menuOptionRequest);
  }

  @CircuitBreaker(name = API_CLIENT, fallbackMethod = "alternativeUpdate")
  public Mono<Void> update(Long id, MenuOptionRequestDto menuOptionRequest) {
    return MenuOptionV2CallStrategy.RETROFIT.update(injector, id, menuOptionRequest);
  }

  @CircuitBreaker(name = API_CLIENT, fallbackMethod = "alternativeDelete")
  public Mono<Void> delete(Long id) {
    return MenuOptionV2CallStrategy.RETROFIT.delete(injector, id);
  }

  public Mono<MenuOptionDto> alternativeFindById(Long id, Throwable throwable) {
    return MenuOptionV1CallStrategy.RETROFIT.findById(injector, id);
  }

  public Flux<MenuOptionDto> alternativeFindByCategory(String category, Throwable throwable) {
    return MenuOptionV1CallStrategy.RETROFIT.findByCategory(injector, category);
  }

  public Mono<Void> alternativeSave(MenuOptionRequestDto menuOptionRequest, Throwable throwable) {
    return MenuOptionV1CallStrategy.RETROFIT.save(injector, menuOptionRequest);
  }

  public Mono<Void> alternativeUpdate(Long id, MenuOptionRequestDto menuOptionRequest, Throwable throwable) {
    return MenuOptionV1CallStrategy.RETROFIT.update(injector, id, menuOptionRequest);
  }

  public Mono<Void> alternativeDelete(Long id, Throwable throwable) {
    return MenuOptionV1CallStrategy.RETROFIT.delete(injector, id);
  }
}
