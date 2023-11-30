package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionSaveRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionUpdateRequestDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MenuApiConnector {

  Flux<MenuOptionDto> findByCategory(String category);

  Mono<MenuOptionDto> findByProductCode(String productCode);

  Mono<Void> save(MenuOptionSaveRequestDto menuOption);

  Mono<Void> update(String productCode, MenuOptionUpdateRequestDto menuOption);

  Mono<Void> delete(String productCode);

  boolean supports(Class<?> selectedClass);
}
