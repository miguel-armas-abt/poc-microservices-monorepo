package com.demo.poc.entrypoint.table.placement.service;

import com.demo.poc.entrypoint.table.placement.dto.request.MenuOrderDto;
import com.demo.poc.entrypoint.table.placement.dto.response.PlacementResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlacementService {

  Mono<Void> generateTableOrder(Flux<MenuOrderDto> requestedMenuOrderList, Integer tableNumber);

  Mono<Void> cleanTable(Integer tableNumber);

  Mono<PlacementResponseDto> findByTableNumber(Integer tableNumber);
}
