package com.demo.bbq.entrypoint.table.placement.service;

import com.demo.bbq.entrypoint.table.placement.dto.request.MenuOrderDTO;
import com.demo.bbq.entrypoint.table.placement.dto.response.PlacementResponseDTO;
import com.demo.bbq.entrypoint.table.placement.repository.TableOrderRepository;
import com.demo.bbq.entrypoint.table.placement.mapper.PlacementMapper;
import com.demo.bbq.entrypoint.table.placement.repository.document.MenuOrderDocument;
import com.demo.bbq.entrypoint.table.placement.repository.document.TableDocument;
import com.demo.bbq.commons.errors.exceptions.BusinessException;
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
  public Mono<PlacementResponseDTO> findByTableNumber(Integer tableNumber) {
    return tableOrderRepository.findByTableNumber(tableNumber)
        .map(placementMapper::toResponseDTO)
        .switchIfEmpty(Mono.error(() -> new BusinessException("TableNotFound", "The table does not exist")));
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
  public Mono<Void> generateTableOrder(Flux<MenuOrderDTO> requestedMenuOrders, Integer tableNumber) {
    return tableOrderRepository.findByTableNumber(tableNumber)
        .flatMap(savedTableOrder -> updateExistingTableOrder(savedTableOrder, requestedMenuOrders))
        .flatMap(tableOrderRepository::save)
        .then(Mono.empty());
  }

  private Mono<TableDocument> updateExistingTableOrder(TableDocument tableOrder, Flux<MenuOrderDTO> requestedMenuOrders) {
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

  private final BiConsumer<Map<String, MenuOrderDocument>, MenuOrderDTO> increaseQuantity = (existingMenuOrderMap, requestedMenuOrder) -> {
    MenuOrderDocument existingMenuOrder = existingMenuOrderMap.get(requestedMenuOrder.getProductCode());
    existingMenuOrder.setQuantity(existingMenuOrder.getQuantity() + requestedMenuOrder.getQuantity());
    existingMenuOrderMap.put(requestedMenuOrder.getProductCode(), existingMenuOrder);
  };

}
