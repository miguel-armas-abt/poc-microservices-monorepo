package com.demo.bbq.business.diningroomorder.infrastructure.repository.database.impl;

import com.demo.bbq.business.diningroomorder.infrastructure.repository.database.TableRepository;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.database.entity.DiningRoomTableEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class TableRepositoryReactive {

  private final TableRepository tableRepository;

  public Mono<DiningRoomTableEntity> findByTableNumber(Integer tableNumber) {
    return tableRepository.findByTableNumber(tableNumber)
        .map(Mono::just)
        .orElse(Mono.empty());
  }

  public Mono<DiningRoomTableEntity> save(DiningRoomTableEntity table) {
    return Mono.just(tableRepository.save(table));
  }
}
