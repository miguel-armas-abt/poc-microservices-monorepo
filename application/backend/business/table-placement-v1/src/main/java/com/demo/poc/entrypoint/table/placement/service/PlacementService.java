package com.demo.poc.entrypoint.table.placement.service;

import com.demo.poc.entrypoint.table.placement.dto.request.MenuOrderDTO;
import com.demo.poc.entrypoint.table.placement.dto.response.PlacementResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlacementService {

  Mono<Void> generateTableOrder(Flux<MenuOrderDTO> requestedMenuOrderList, Integer tableNumber);

  Mono<Void> cleanTable(Integer tableNumber);

  Mono<PlacementResponseDTO> findByTableNumber(Integer tableNumber);
}
