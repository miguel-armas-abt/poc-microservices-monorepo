package com.demo.bbq.entrypoint.tableorder.service;

import com.demo.bbq.entrypoint.menu.repository.MenuRepositoryStrategy;
import com.demo.bbq.entrypoint.tableorder.dto.MenuOrderRequestDTO;
import com.demo.bbq.entrypoint.tableorder.repository.TableOrderRepository;
import com.demo.bbq.entrypoint.tableorder.repository.wrapper.TableOrderResponseWrapper;
import com.demo.bbq.commons.errors.exceptions.BusinessException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Service
public class TablePlacementServiceImpl implements TablePlacementService {

  private final TableOrderRepository tableOrderRepository;
  private final MenuRepositoryStrategy menuRepositoryStrategy;

  @Override
  public Mono<Void> generateTableOrder(ServerRequest serverRequest,
                                       List<MenuOrderRequestDTO> requestedMenuOrders,
                                       Integer tableNumber) {
    return Flux.fromIterable(requestedMenuOrders)
        .flatMap(menuOrder -> existsMenuOption(serverRequest, menuOrder))
        .then(tableOrderRepository.generateTableOrder(serverRequest, requestedMenuOrders, tableNumber))
        .ignoreElement();
  }

  private Mono<Void> existsMenuOption(ServerRequest httpRequest, MenuOrderRequestDTO menuOrderRequest) {
    return menuRepositoryStrategy
        .getService()
        .findByProductCode(httpRequest, menuOrderRequest.getProductCode())
        .switchIfEmpty(Mono.error(new BusinessException("MenuOptionNotFound")))
        .then();
  }

  @Override
  public Mono<TableOrderResponseWrapper> findByTableNumber(ServerRequest httpRequest, Integer tableNumber) {
    return tableOrderRepository.findByTableNumber(httpRequest, tableNumber);
  }

}