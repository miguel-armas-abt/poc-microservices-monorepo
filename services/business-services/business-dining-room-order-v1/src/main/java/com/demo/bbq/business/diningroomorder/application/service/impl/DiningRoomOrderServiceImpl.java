package com.demo.bbq.business.diningroomorder.application.service.impl;

import com.demo.bbq.business.diningroomorder.domain.model.response.DiningRoomOrder;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.database.entity.DiningRoomTableEntity;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.database.entity.MenuOrderEntity;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.database.impl.MenuOrderRepositoryReactive;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.database.impl.TableRepositoryReactive;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.MenuOptionApi;
import com.demo.bbq.business.diningroomorder.application.service.DiningRoomOrderService;
import com.demo.bbq.business.diningroomorder.domain.exception.DiningRoomOrderException;
import com.demo.bbq.business.diningroomorder.domain.model.request.MenuOrderRequest;
import com.demo.bbq.business.diningroomorder.infrastructure.mapper.DiningRoomOrderMapper;
import com.demo.bbq.business.diningroomorder.infrastructure.mapper.MenuOrderMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class DiningRoomOrderServiceImpl implements DiningRoomOrderService {

  private final TableRepositoryReactive tableRepository;
  private final MenuOrderRepositoryReactive menuOrderRepository;

  private final MenuOptionApi menuOptionApi;

  private final DiningRoomOrderMapper diningRoomOrderMapper;

  private final MenuOrderMapper menuOrderMapper;

  @Override
  public Mono<DiningRoomOrder> findByTableNumber(Integer tableNumber) {
    return tableRepository.findByTableNumber(tableNumber)
        .map(diningRoomOrderMapper::fromEntityToDomain)
        .switchIfEmpty(Mono.error(DiningRoomOrderException.ERROR1000.buildCustomException()));
  }

  @Override
  public Mono<Long> generateTableOrder(List<MenuOrderRequest> menuOrderRequestList, Integer tableNumber) {
    return tableRepository.findByTableNumber(tableNumber)
        .flatMap(table -> Flux.fromIterable(menuOrderRequestList)
            .map(menuOptionRequest -> menuOptionApi.findById(menuOptionRequest.getMenuOptionId())
                .onErrorResume(ex -> Mono.error(DiningRoomOrderException.ERROR1001.buildCustomException("id: " + menuOptionRequest.getMenuOptionId())))
                .ignoreElement()
                .then(menuOrderRepository.saveMenuOrder(menuOrderMapper.fromRequestToEntity(menuOptionRequest, table.getId()))))
            .ignoreElements()
            .then(tableRepository.save(table)
                .map(DiningRoomTableEntity::getId)));

// si se agrega a la orden más opciones de menu del mismo tipo (mismo menuOptionId) entonces chancar el registro que ya existía en bd
//
//    return this.findDiningRoomTableByTableNumber(tableNumber)
//        .flatMap(diningRoomTable -> Flux.fromIterable(menuOrderRequestList)
//            .flatMap(menuOrderRequest -> this.findMenuOptionById(menuOrderRequest.getMenuOptionId())
//                .onErrorResume(ex -> Mono.error(ExceptionCatalog.ERROR1001.buildCustomException("id: " + menuOrderRequest.getMenuOptionId())))
//                .flatMapMany(menuOptionFound -> Flux.fromIterable(diningRoomTable.getMenuOrderList())
//                    .doOnNext(x -> log.info("diningRoomTable-MenuOrder: " + new com.google.gson.Gson().toJson(x)))
//                    .flatMap(previousMenuOrder -> this.validateAndSaveMenuOrder(previousMenuOrder.getMenuOptionId(), diningRoomTable.getId(), menuOrderRequest))
//                    .switchIfEmpty(this.validateAndSaveMenuOrder(menuOrderMapper.fromRequestToEntity(menuOrderRequest, diningRoomTable.getId())))
//                ))
//            .flatMap(savedMenuOrder -> Mono.just(diningRoomTable))
//            .next())
//        .flatMap(diningRoomTable -> this.saveDiningRoomTable(diningRoomTable)
//            .map(DiningRoomTable::getId));
  }
  private Mono<MenuOrderEntity> validateAndSaveMenuOrder(Long previousMenuOrderId, Long diningRoomTableId, MenuOrderRequest menuOrderRequest) {
    return (!previousMenuOrderId.equals(menuOrderRequest.getMenuOptionId()))
        ? this.addToPreviousOrder(menuOrderRequest).doOnError(ex -> log.info("error addToPreviousOrder"))
        : menuOrderRepository.saveMenuOrder(menuOrderMapper.fromRequestToEntity(menuOrderRequest, diningRoomTableId)).doOnError(ex -> log.info("error saveMenuOrder"));
  }

  private Mono<MenuOrderEntity> addToPreviousOrder(MenuOrderRequest menuOrderRequest) {
    return menuOrderRepository.findByMenuOptionId(menuOrderRequest.getMenuOptionId())
        .flatMap(savedMenuOrder -> {
          Integer actualQuantity = savedMenuOrder.getQuantity() + menuOrderRequest.getQuantity();
          savedMenuOrder.setQuantity(actualQuantity);
          return menuOrderRepository.saveMenuOrder(savedMenuOrder);
        });
  }

}
