package com.demo.bbq.business.tableplacement.application.service;

import com.demo.bbq.business.tableplacement.domain.model.request.MenuOrderRequest;
import com.demo.bbq.business.tableplacement.domain.model.response.TableOrder;
import reactor.core.publisher.Mono;
import java.util.List;

public interface TablePlacementService {

  Mono<Void> generateTableOrder(List<MenuOrderRequest> requestedMenuOrderList, Integer tableNumber);

  Mono<Void> cleanTable(Integer tableNumber);

  Mono<TableOrder> findByTableNumber(Integer tableNumber);
}
