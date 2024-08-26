package com.demo.bbq.entrypoint.tableorder.service;

import com.demo.bbq.entrypoint.menu.repository.MenuRepositoryStrategy;
import com.demo.bbq.entrypoint.tableorder.dto.request.MenuOrderRequestDTO;
import com.demo.bbq.entrypoint.tableorder.repository.TableOrderRepository;
import com.demo.bbq.entrypoint.tableorder.repository.wrapper.TableOrderResponseWrapper;
import com.demo.bbq.commons.errors.exceptions.BusinessException;
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
  private final MenuRepositoryStrategy menuRepositoryStrategy;

  @Override
  public Mono<Void> generateTableOrder(Map<String, String> headers,
                                       List<MenuOrderRequestDTO> requestedMenuOrders,
                                       Integer tableNumber) {
    return Flux.fromIterable(requestedMenuOrders)
        .flatMap(menuOrder -> existsMenuOption(headers, menuOrder))
        .then(tableOrderRepository.generateTableOrder(headers, requestedMenuOrders, tableNumber))
        .ignoreElement();
  }

  private Mono<Void> existsMenuOption(Map<String, String> headers, MenuOrderRequestDTO menuOrderRequest) {
    return menuRepositoryStrategy
        .getService()
        .findByProductCode(headers, menuOrderRequest.getProductCode())
        .switchIfEmpty(Mono.error(new BusinessException("MenuOptionNotFound")))
        .then();
  }

  @Override
  public Mono<TableOrderResponseWrapper> findByTableNumber(Map<String, String> headers, Integer tableNumber) {
    return tableOrderRepository.findByTableNumber(headers, tableNumber);
  }

}