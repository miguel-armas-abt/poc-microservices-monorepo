package com.demo.bbq.entrypoint.menu.repository;

import com.demo.bbq.commons.toolkit.serviceselector.SelectedServiceBase;
import com.demo.bbq.entrypoint.menu.repository.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.entrypoint.menu.repository.wrapper.request.MenuOptionSaveRequestWrapper;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MenuRepository extends SelectedServiceBase {

  Mono<MenuOptionResponseWrapper> findByProductCode(ServerRequest serverRequest, String productCode);

  Flux<MenuOptionResponseWrapper> findByCategory(ServerRequest serverRequest, String category);

  Mono<Void> save(ServerRequest serverRequest, MenuOptionSaveRequestWrapper menuOption);

  Mono<Void> update(ServerRequest serverRequest, String productCode, MenuOptionSaveRequestWrapper menuOption);

  Mono<Void> delete(ServerRequest serverRequest, String productCode, MenuOptionSaveRequestWrapper menuOption);

}
