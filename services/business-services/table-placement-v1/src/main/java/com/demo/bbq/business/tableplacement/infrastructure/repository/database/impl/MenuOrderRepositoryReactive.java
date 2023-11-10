package com.demo.bbq.business.tableplacement.infrastructure.repository.database.impl;

import com.demo.bbq.business.tableplacement.infrastructure.repository.database.MenuOrderRepository;
import com.demo.bbq.business.tableplacement.infrastructure.repository.database.entity.MenuOrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class MenuOrderRepositoryReactive {

  private final MenuOrderRepository menuOrderRepository;

  public Mono<MenuOrderEntity> updateMenuOrder(MenuOrderEntity menuOrder) {
    return Mono.just(menuOrder)
        .map(existingMenuOrder -> menuOrderRepository.findByMenuOptionId(existingMenuOrder.getMenuOptionId())
            .map(foundMenuOrder -> {
              foundMenuOrder.setMenuOptionId(existingMenuOrder.getMenuOptionId());
              foundMenuOrder.setQuantity(existingMenuOrder.getQuantity());
              foundMenuOrder.setTableId(existingMenuOrder.getTableId());
              return foundMenuOrder;
            })
            .orElse(existingMenuOrder))
        .map(menuOrderRepository::save);
  }
}
