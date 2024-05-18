package com.demo.bbq.repository.menu;

import com.demo.bbq.application.helper.serviceselector.SelectedServiceBase;
import com.demo.bbq.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.repository.menu.wrapper.request.MenuOptionSaveRequestWrapper;
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
