package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menuoption;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menuoption.dto.MenuOptionDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menuoption.dto.MenuOptionRequestDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MenuOptionApiConnector {

  Flux<MenuOptionDto> findByCategory(String category);

  Mono<MenuOptionDto> findById(Long id);

  Mono<Void> save(MenuOptionRequestDto menuOption);

  Mono<Void> update(Long id, MenuOptionRequestDto menuOption);

  Mono<Void> delete(Long id);

  boolean supports(Class<?> selectedClass);
}
