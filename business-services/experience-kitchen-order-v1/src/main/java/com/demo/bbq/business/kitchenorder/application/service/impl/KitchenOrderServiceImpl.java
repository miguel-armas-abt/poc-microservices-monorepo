package com.demo.bbq.business.kitchenorder.application.service.impl;

import com.demo.bbq.business.kitchenorder.application.service.KitchenOrderService;
import com.demo.bbq.business.kitchenorder.domain.model.response.MenuOrder;
import com.demo.bbq.business.kitchenorder.infrastructure.repository.diningroomorder.DiningRoomOrderRepository;
import com.demo.bbq.business.kitchenorder.infrastructure.repository.menuoptionv2.MenuOptionRepository;
import io.smallrye.mutiny.Multi;
import javax.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@Slf4j
@ApplicationScoped
public class KitchenOrderServiceImpl implements KitchenOrderService {

  @RestClient
  MenuOptionRepository menuOptionRepository;

  @RestClient
  DiningRoomOrderRepository diningRoomOrderRepository;

  @Override
  public Multi<MenuOrder> findByCategory(Integer tableNumber) {
    return diningRoomOrderRepository.findByTableNumber(tableNumber)
        .flatMap(diningRoomOrder -> Multi.createFrom().iterable(diningRoomOrder.getMenuOrderList())
            .toUni()
            .flatMap(menuOrder -> menuOptionRepository.findById(menuOrder.getMenuOptionId())
                .map(menuOption -> MenuOrder.builder()
                    .description(menuOption.getDescription())
                    .category(menuOption.getCategory())
                    .quantity(menuOrder.getQuantity())
                    .isCooking(Boolean.FALSE)
                    .build())))
        .toMulti();
  }
}
