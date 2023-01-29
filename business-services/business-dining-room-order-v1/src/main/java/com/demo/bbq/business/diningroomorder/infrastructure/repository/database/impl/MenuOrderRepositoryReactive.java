package com.demo.bbq.business.diningroomorder.infrastructure.repository.database.impl;

import com.demo.bbq.business.diningroomorder.infrastructure.repository.database.MenuOrderRepository;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.database.entity.MenuOrderEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class MenuOrderRepositoryReactive {

  private final MenuOrderRepository menuOrderRepository;

  public Mono<MenuOrderEntity> saveMenuOrder(MenuOrderEntity menuOrder) {
    return Mono.just(menuOrderRepository.save(menuOrder));
  }

  public Mono<MenuOrderEntity> findByMenuOptionId(Long menuOptionId) {
    return menuOrderRepository.findByMenuOptionId(menuOptionId)
        .map(Mono::just)
        .orElse(Mono.empty());
  }
}
