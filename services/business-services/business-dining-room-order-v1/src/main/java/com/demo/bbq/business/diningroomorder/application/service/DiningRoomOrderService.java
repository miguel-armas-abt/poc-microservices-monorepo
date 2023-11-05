package com.demo.bbq.business.diningroomorder.application.service;

import com.demo.bbq.business.diningroomorder.domain.model.request.MenuOrderRequest;
import com.demo.bbq.business.diningroomorder.domain.model.dto.DiningRoomOrderDto;
import reactor.core.publisher.Mono;
import java.util.List;

public interface DiningRoomOrderService {

  Mono<Void> generateTableOrder(List<MenuOrderRequest> requestedMenuOrderList, Integer tableNumber);

  Mono<Void> cleanTable(Integer tableNumber);

  Mono<DiningRoomOrderDto> findByTableNumber(Integer tableNumber);
}
