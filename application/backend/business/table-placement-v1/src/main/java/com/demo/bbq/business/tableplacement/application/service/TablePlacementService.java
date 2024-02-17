package com.demo.bbq.business.tableplacement.application.service;

import com.demo.bbq.business.tableplacement.application.dto.tableplacement.request.MenuOrderRequest;
import com.demo.bbq.business.tableplacement.application.dto.tableplacement.response.TablePlacementResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TablePlacementService {

  Mono<Void> generateTableOrder(Flux<MenuOrderRequest> requestedMenuOrderList, Integer tableNumber);

  Mono<Void> cleanTable(Integer tableNumber);

  Mono<TablePlacementResponse> findByTableNumber(Integer tableNumber);
}
