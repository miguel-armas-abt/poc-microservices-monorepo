package com.demo.bbq.business.tableplacement.application.service.impl;

import com.demo.bbq.business.tableplacement.application.dto.tableplacement.response.TablePlacementResponse;
import com.demo.bbq.business.tableplacement.application.service.TablePlacementService;
import com.demo.bbq.business.tableplacement.domain.repository.tableorder.TableOrderRepository;
import com.demo.bbq.business.tableplacement.domain.exception.TablePlacementExceptionEnum;
import com.demo.bbq.business.tableplacement.application.dto.tableplacement.request.MenuOrderRequest;
import com.demo.bbq.business.tableplacement.application.mapper.TablePlacementMapper;
import com.demo.bbq.business.tableplacement.domain.repository.tableorder.document.MenuOrderDocument;
import com.demo.bbq.business.tableplacement.domain.repository.tableorder.document.TableDocument;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class TablePlacementServiceImpl implements TablePlacementService {

  private final TableOrderRepository tableOrderRepository;
  private final TablePlacementMapper tablePlacementMapper;

  @Override
  public Mono<TablePlacementResponse> findByTableNumber(Integer tableNumber) {
    return tableOrderRepository.findByTableNumber(tableNumber)
        .map(tablePlacementMapper::fromDocumentToResponse)
        .switchIfEmpty(Mono.error(TablePlacementExceptionEnum.ERROR0000.buildException()));
  }

  @Override
  public Mono<Void> cleanTable(Integer tableNumber) {
    return tableOrderRepository.findByTableNumber(tableNumber)
        .map(tableEntity -> {
          tableEntity.setMenuOrderList(new ArrayList<>());
          return tableEntity;
        })
        .flatMap(tableOrderRepository::save)
        .ignoreElement()
        .flatMap(ignored -> Mono.empty());
  }

  @Override
  public Mono<Void> generateTableOrder(Flux<MenuOrderRequest> requestedMenuOrders, Integer tableNumber) {
    return tableOrderRepository.findByTableNumber(tableNumber)
        .flatMap(savedTableOrder -> updateExistingTableOrder(savedTableOrder, requestedMenuOrders))
        .flatMap(tableOrderRepository::save)
        .then(Mono.empty());
  }

  private Mono<TableDocument> updateExistingTableOrder(TableDocument tableOrder, Flux<MenuOrderRequest> requestedMenuOrders) {
    return requestedMenuOrders.collectList()
        .map(requestedMenuOrderList -> {
          Map<String, MenuOrderDocument> existingMenuOrderMap = tableOrder.getMenuOrderList()
              .stream()
              .collect(Collectors.toMap(MenuOrderDocument::getProductCode, savedMenuOrder -> savedMenuOrder));

          requestedMenuOrderList.forEach(requestedMenuOrder -> {
            if (menuOptionAlreadyExist.test(existingMenuOrderMap, requestedMenuOrder.getProductCode())) {
              increaseQuantity.accept(existingMenuOrderMap, requestedMenuOrder);
            } else {
              existingMenuOrderMap.put(requestedMenuOrder.getProductCode(), tablePlacementMapper.fromRequestToDocument(requestedMenuOrder));
            }
          });
          tableOrder.setMenuOrderList(new ArrayList<>(existingMenuOrderMap.values()));
          return tableOrder;
        });
  }

  private final BiPredicate<Map<String, MenuOrderDocument>, String> menuOptionAlreadyExist = Map::containsKey;

  private final BiConsumer<Map<String, MenuOrderDocument>, MenuOrderRequest> increaseQuantity = (existingMenuOrderMap, requestedMenuOrder) -> {
    MenuOrderDocument existingMenuOrder = existingMenuOrderMap.get(requestedMenuOrder.getProductCode());
    existingMenuOrder.setQuantity(existingMenuOrder.getQuantity() + requestedMenuOrder.getQuantity());
    existingMenuOrderMap.put(requestedMenuOrder.getProductCode(), existingMenuOrder);
  };

}
