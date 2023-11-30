package com.demo.bbq.business.tableplacement.infrastructure.repository.database.impl;

import com.demo.bbq.business.tableplacement.infrastructure.repository.database.TableRepository;
import com.demo.bbq.business.tableplacement.infrastructure.repository.database.entity.TableEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class TableRepositoryReactive {

  private final TableRepository tableRepository;

  public Mono<TableEntity> findByTableNumber(Integer tableNumber) {
    return tableRepository.findByTableNumber(tableNumber)
        .map(Mono::just)
        .orElse(Mono.empty());
  }

  public Mono<TableEntity> save(TableEntity table) {
    return Mono.just(tableRepository.save(table));
  }
}
