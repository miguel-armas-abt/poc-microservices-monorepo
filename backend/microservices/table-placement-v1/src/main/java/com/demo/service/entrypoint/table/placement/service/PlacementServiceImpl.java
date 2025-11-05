package com.demo.service.entrypoint.table.placement.service;

import com.demo.service.commons.exceptions.TableNotFoundException;
import com.demo.service.entrypoint.table.placement.dto.request.MenuOrderDto;
import com.demo.service.entrypoint.table.placement.dto.response.PlacementResponseDto;
import com.demo.service.entrypoint.table.placement.repository.TableOrderRepository;
import com.demo.service.entrypoint.table.placement.mapper.PlacementMapper;
import com.demo.service.entrypoint.table.placement.repository.document.MenuOrderDocument;
import com.demo.service.entrypoint.table.placement.repository.document.TableDocument;
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
public class PlacementServiceImpl implements PlacementService {

  private final TableOrderRepository tableOrderRepository;
  private final PlacementMapper placementMapper;

  @Override
  public Mono<PlacementResponseDto> findByTableNumber(Integer tableNumber) {
    return tableOrderRepository.findByTableNumber(tableNumber)
        .map(placementMapper::toResponseDTO)
        .switchIfEmpty(Mono.error(TableNotFoundException::new));
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
  public Mono<Void> generateTableOrder(Flux<MenuOrderDto> requestedMenuOrders, Integer tableNumber) {
    return tableOrderRepository.findByTableNumber(tableNumber)
        .flatMap(savedTableOrder -> updateExistingTableOrder(savedTableOrder, requestedMenuOrders))
        .flatMap(tableOrderRepository::save)
        .then(Mono.empty());
  }

  private Mono<TableDocument> updateExistingTableOrder(TableDocument tableOrder, Flux<MenuOrderDto> requestedMenuOrders) {
    return requestedMenuOrders.collectList()
        .map(requestedMenuOrderList -> {
          Map<String, MenuOrderDocument> existingMenuOrderMap = tableOrder.getMenuOrderList()
              .stream()
              .collect(Collectors.toMap(MenuOrderDocument::getProductCode, savedMenuOrder -> savedMenuOrder));

          requestedMenuOrderList.forEach(requestedMenuOrder -> {
            if (menuOptionAlreadyExist.test(existingMenuOrderMap, requestedMenuOrder.getProductCode())) {
              increaseQuantity.accept(existingMenuOrderMap, requestedMenuOrder);
            } else {
              existingMenuOrderMap.put(requestedMenuOrder.getProductCode(), placementMapper.toDocument(requestedMenuOrder));
            }
          });
          tableOrder.setMenuOrderList(new ArrayList<>(existingMenuOrderMap.values()));
          return tableOrder;
        });
  }

  private final BiPredicate<Map<String, MenuOrderDocument>, String> menuOptionAlreadyExist = Map::containsKey;

  private final BiConsumer<Map<String, MenuOrderDocument>, MenuOrderDto> increaseQuantity = (existingMenuOrderMap, requestedMenuOrder) -> {
    MenuOrderDocument existingMenuOrder = existingMenuOrderMap.get(requestedMenuOrder.getProductCode());
    existingMenuOrder.setQuantity(existingMenuOrder.getQuantity() + requestedMenuOrder.getQuantity());
    existingMenuOrderMap.put(requestedMenuOrder.getProductCode(), existingMenuOrder);
  };

}
