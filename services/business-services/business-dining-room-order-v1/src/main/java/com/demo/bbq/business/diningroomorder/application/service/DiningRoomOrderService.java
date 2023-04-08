package com.demo.bbq.business.diningroomorder.application.service;

import com.demo.bbq.business.diningroomorder.domain.model.request.MenuOrderRequest;
import com.demo.bbq.business.diningroomorder.domain.model.response.DiningRoomOrder;
import reactor.core.publisher.Mono;
import java.util.List;

/**
 * <br/>Interface Service que define los métodos necesarios para tramitar la lógica de negocio del contexto Dining Room
 * Order.<br/>
 *
 * @author Miguel Armas Abt <br/>
 */
public interface DiningRoomOrderService {

  Mono<Long> generateTableOrder(List<MenuOrderRequest> menuOrderRequestList, Integer tableNumber);

  Mono<DiningRoomOrder> findByTableNumber(Integer tableNumber);
}
