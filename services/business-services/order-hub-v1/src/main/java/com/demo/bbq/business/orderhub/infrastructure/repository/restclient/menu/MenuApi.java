package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionSaveRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionUpdateRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.properties.MenuOptionSelectorClassProperties;
import com.demo.bbq.support.exception.model.ApiException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.List;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

  @CircuitBreaker(name = API_CLIENT, fallbackMethod = "findByProductCodeFallback")
  public Mono<MenuOptionDto> findByProductCode(String productCode) {
    return getService(properties.getSelectorClass()).findByProductCode(productCode);
  }

  @CircuitBreaker(name = API_CLIENT, fallbackMethod = "findByCategoryFallback")
  public Flux<MenuOptionDto> findByCategory(String category) {
    return getService(properties.getSelectorClass()).findByCategory(category);
  }

  @CircuitBreaker(name = API_CLIENT, fallbackMethod = "saveFallback")
  public Mono<Void> save(MenuOptionSaveRequestDto menuOptionRequest) {
    return getService(properties.getSelectorClass()).save(menuOptionRequest);
  }

  @CircuitBreaker(name = API_CLIENT, fallbackMethod = "updateFallback")
  public Mono<Void> update(String productCode, MenuOptionUpdateRequestDto menuOptionRequest) {
    return getService(properties.getSelectorClassFallback()).update(productCode, menuOptionRequest);
  }

  @CircuitBreaker(name = API_CLIENT, fallbackMethod = "deleteFallback")
  public Mono<Void> delete(String productCode) {
    return getService(properties.getSelectorClassFallback()).delete(productCode);
  }

  public Mono<MenuOptionDto> findByProductCodeFallback(String productCode, Throwable throwable) {
    validateException(throwable);
    return getService(properties.getSelectorClassFallback()).findByProductCode(productCode);
  }

  public Flux<MenuOptionDto> findByCategoryFallback(String category, Throwable throwable) {
    validateException(throwable);
    return getService(properties.getSelectorClassFallback()).findByCategory(category);
  }

  public Mono<Void> saveFallback(MenuOptionSaveRequestDto menuOptionRequest, Throwable throwable) {
    validateException(throwable);
    return getService(properties.getSelectorClassFallback()).save(menuOptionRequest);
  }

  public Mono<Void> updateFallback(String productCode, MenuOptionUpdateRequestDto menuOptionRequest, Throwable throwable) {
    validateException(throwable);
    return getService(properties.getSelectorClassFallback()).update(productCode, menuOptionRequest);
  }

  public Mono<Void> deleteFallback(String productCode, Throwable throwable) {
    validateException(throwable);
    return getService(properties.getSelectorClassFallback()).delete(productCode);
  }

  private static void validateException(Throwable throwable) {
    if (throwable instanceof ApiException) {
      ApiException apiException = (ApiException) throwable;
      if(isFunctionalError.test(apiException)) {
        throw apiException;
      }
    }
  }

  private static final Predicate<ApiException> isFunctionalError = apiException ->
      apiException.getHttpStatus().equals(HttpStatus.BAD_REQUEST) || apiException.getHttpStatus().equals(HttpStatus.NOT_FOUND);
}
