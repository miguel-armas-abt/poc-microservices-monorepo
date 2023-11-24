package com.demo.bbq.business.tableplacement.application.service.impl;

import com.demo.bbq.business.tableplacement.domain.model.response.TableOrder;
import com.demo.bbq.business.tableplacement.domain.model.response.MenuOrder;
import com.demo.bbq.business.tableplacement.infrastructure.repository.database.impl.MenuOrderRepositoryReactive;
import com.demo.bbq.business.tableplacement.infrastructure.repository.database.impl.TableRepositoryReactive;
import com.demo.bbq.business.tableplacement.application.service.TablePlacementService;
import com.demo.bbq.business.tableplacement.domain.exception.TablePlacementException;
import com.demo.bbq.business.tableplacement.domain.model.request.MenuOrderRequest;
import com.demo.bbq.business.tableplacement.infrastructure.mapper.TableOrderMapper;
import com.demo.bbq.business.tableplacement.infrastructure.mapper.MenuOrderMapper;
import java.util.ArrayList;
import java.util.List;
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

  private final TableRepositoryReactive tableRepository;
  private final MenuOrderRepositoryReactive menuOrderRepository;

  private final TableOrderMapper tableOrderMapper;

  private final MenuOrderMapper menuOrderMapper;

  @Override
  public Mono<TableOrder> findByTableNumber(Integer tableNumber) {
    return tableRepository.findByTableNumber(tableNumber)
        .map(tableOrderMapper::fromEntityToDto)
        .switchIfEmpty(Mono.error(TablePlacementException.ERROR0000.buildException()));
  }

  @Override
  public Mono<Void> cleanTable(Integer tableNumber) {
    return tableRepository.findByTableNumber(tableNumber)
        .map(tableEntity -> {
          tableEntity.setMenuOrderList(new ArrayList<>());
          return tableEntity;
        })
        .flatMap(tableRepository::save)
        .ignoreElement()
        .flatMap(ignored -> Mono.empty());
  }

  @Override
  public Mono<Void> generateTableOrder(List<MenuOrderRequest> requestedMenuOrderList, Integer tableNumber) {
    return findByTableNumber(tableNumber)
        .map(diningRoomOrder -> updateExistingDiningRoomOrder(diningRoomOrder, requestedMenuOrderList))
        .flatMapMany(diningRoomOrder -> Flux.fromIterable(diningRoomOrder.getMenuOrderList())
            .flatMap(menuOrder -> menuOrderRepository.updateMenuOrder(menuOrderMapper.fromDtoToEntity(menuOrder, diningRoomOrder.getId()))))
        .ignoreElements()
        .flatMap(ignored -> Mono.empty());
  }

  private TableOrder updateExistingDiningRoomOrder(TableOrder diningRoomOrder, List<MenuOrderRequest> requestedMenuOrderList) {
    Map<String, MenuOrder> existingMenuOrderMap = diningRoomOrder.getMenuOrderList()
        .stream()
        .collect(Collectors.toMap(MenuOrder::getProductCode, savedMenuOrder -> savedMenuOrder));

    requestedMenuOrderList.forEach(requestedMenuOrder -> {
        if (menuOptionAlreadyExist.test(existingMenuOrderMap, requestedMenuOrder.getProductCode())) {
          increaseQuantity.accept(existingMenuOrderMap, requestedMenuOrder);
        } else {
          existingMenuOrderMap.put(requestedMenuOrder.getProductCode(), menuOrderMapper.fromRequestToDto(requestedMenuOrder));
        }
    });
    diningRoomOrder.setMenuOrderList(new ArrayList<>(existingMenuOrderMap.values()));
    return diningRoomOrder;
  }

  private final BiPredicate<Map<String, MenuOrder>, String> menuOptionAlreadyExist = Map::containsKey;

  private final BiConsumer<Map<String, MenuOrder>, MenuOrderRequest> increaseQuantity = (existingMenuOrderMap, requestedMenuOrder) -> {
    MenuOrder existingMenuOrder = existingMenuOrderMap.get(requestedMenuOrder.getProductCode());
    existingMenuOrder.setQuantity(existingMenuOrder.getQuantity() + requestedMenuOrder.getQuantity());
    existingMenuOrderMap.put(requestedMenuOrder.getProductCode(), existingMenuOrder);
  };

}
