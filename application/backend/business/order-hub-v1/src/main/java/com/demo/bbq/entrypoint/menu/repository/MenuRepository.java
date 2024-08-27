package com.demo.bbq.entrypoint.menu.repository;

import com.demo.bbq.entrypoint.menu.repository.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.entrypoint.menu.repository.wrapper.request.MenuOptionSaveRequestWrapper;
import java.util.Map;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MenuRepository {

  Mono<MenuOptionResponseWrapper> findByProductCode(Map<String, String> headers, String productCode);

  Flux<MenuOptionResponseWrapper> findByCategory(Map<String, String> headers, String category);

  Mono<Void> save(Map<String, String> headers, MenuOptionSaveRequestWrapper menuOption);

  Mono<Void> update(Map<String, String> headers, String productCode, MenuOptionSaveRequestWrapper menuOption);

  Mono<Void> delete(Map<String, String> headers, String productCode, MenuOptionSaveRequestWrapper menuOption);

  boolean supports(Class<?> selectedClass);
}
