package com.demo.poc.entrypoint.tableorder.service;

import com.demo.poc.commons.custom.exceptions.MenuOptionNotFoundException;
import com.demo.poc.entrypoint.menu.repository.MenuRepositorySelector;
import com.demo.poc.entrypoint.tableorder.dto.request.MenuOrderRequestDto;
import com.demo.poc.entrypoint.tableorder.repository.TableOrderRepository;
import com.demo.poc.entrypoint.tableorder.repository.wrapper.TableOrderResponseWrapper;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class TablePlacementServiceImpl implements TablePlacementService {

  private final TableOrderRepository tableOrderRepository;
  private final MenuRepositorySelector menuRepositorySelector;

  @Override
  public Mono<Void> generateTableOrder(Map<String, String> headers,
                                       List<MenuOrderRequestDto> requestedMenuOrders,
                                       Integer tableNumber) {
    return Flux.fromIterable(requestedMenuOrders)
        .flatMap(menuOrder -> existsMenuOption(headers, menuOrder))
        .then(tableOrderRepository.generateTableOrder(headers, requestedMenuOrders, tableNumber))
        .ignoreElement();
  }

  private Mono<Void> existsMenuOption(Map<String, String> headers, MenuOrderRequestDto menuOrderRequest) {
    return menuRepositorySelector
        .selectStrategy()
        .findByProductCode(headers, menuOrderRequest.getProductCode())
        .switchIfEmpty(Mono.error(new MenuOptionNotFoundException()))
        .then();
  }

  @Override
  public Mono<TableOrderResponseWrapper> findByTableNumber(Map<String, String> headers, Integer tableNumber) {
    return tableOrderRepository.findByTableNumber(headers, tableNumber);
  }

}