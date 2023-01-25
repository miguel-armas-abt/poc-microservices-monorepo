package com.demo.bbq.business.diningroomorder.application.service.impl;

import com.demo.bbq.business.diningroomorder.domain.model.response.DiningRoomOrder;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.database.MenuOrderRepository;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.database.entity.DiningRoomTableEntity;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.database.entity.MenuOrderEntity;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.MenuOptionRepository;
import com.demo.bbq.business.diningroomorder.application.service.DiningRoomOrderService;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.database.TableRepository;
import com.demo.bbq.business.diningroomorder.domain.exception.DiningRoomOrderException;
import com.demo.bbq.business.diningroomorder.infrastructure.mapper.DiningRoomOrderMapper;
import com.demo.bbq.business.diningroomorder.infrastructure.mapper.MenuOrderMapper;
import com.demo.bbq.business.diningroomorder.domain.model.request.MenuOrderRequest;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.dto.MenuOptionDto;

import java.util.List;
import java.util.function.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@Component
public class DiningRoomOrderServiceImpl implements DiningRoomOrderService {

  private final TableRepository tableRepository;
  private final MenuOrderRepository menuOrderRepository;

  private final MenuOptionRepository menuOptionRepository;
  private final DiningRoomOrderMapper diningRoomOrderMapper;

  private final MenuOrderMapper menuOrderMapper;

  @Override
  public Mono<DiningRoomOrder> findByTableNumber(Integer tableNumber) {
    return findDiningRoomTableByTableNumber(tableNumber)
        .map(diningRoomOrderMapper::fromEntityToDomain)
        .switchIfEmpty(Mono.error(DiningRoomOrderException.ERROR1000.buildCustomException()));
  }

  @Override
  public Mono<Long> generateTableOrder(List<MenuOrderRequest> menuOrderRequestList, Integer tableNumber) {
    return this.findDiningRoomTableByTableNumber(tableNumber)
        .flatMap(table -> Flux.fromIterable(menuOrderRequestList)
            .map(menuOptionRequest -> this.findMenuOptionById(menuOptionRequest.getMenuOptionId())
                .onErrorResume(ex -> Mono.error(DiningRoomOrderException.ERROR1001.buildCustomException("id: " + menuOptionRequest.getMenuOptionId())))
                .ignoreElement()
                .then(this.saveMenuOrder(menuOrderMapper.fromRequestToEntity(menuOptionRequest, table.getId()))))
            .ignoreElements()
            .then(this.saveDiningRoomTable(table)
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
        ? this.addToPreviousOrder.apply(menuOrderRequest).doOnError(ex -> log.info("error addToPreviousOrder"))
        : this.saveMenuOrder(menuOrderMapper.fromRequestToEntity(menuOrderRequest, diningRoomTableId)).doOnError(ex -> log.info("error saveMenuOrder"));
  }

  private Function<MenuOrderRequest, Mono<MenuOrderEntity>> addToPreviousOrder = menuOrderRequest ->
      this.findMenuOrderByMenuOptionId(menuOrderRequest.getMenuOptionId())
          .flatMap(savedMenuOrder -> {
            Integer actualQuantity = savedMenuOrder.getQuantity() + menuOrderRequest.getQuantity();
            savedMenuOrder.setQuantity(actualQuantity);
            return this.saveMenuOrder(savedMenuOrder);
          });

  private Mono<DiningRoomTableEntity> findDiningRoomTableByTableNumber(Integer tableNumber) {
      return tableRepository.findByTableNumber(tableNumber)
          .map(Mono::just)
          .orElse(Mono.empty());
  }

  private Mono<MenuOptionDto> findMenuOptionById(Long menuOptionRequestId) {
    return RxJava2Adapter.singleToMono(menuOptionRepository.findById(menuOptionRequestId));
  }

  private Mono<MenuOrderEntity> saveMenuOrder(MenuOrderEntity menuOrder) {
    return Mono.just(menuOrderRepository.save(menuOrder));
  }

  private Mono<DiningRoomTableEntity> saveDiningRoomTable(DiningRoomTableEntity table) {
    return Mono.just(tableRepository.save(table));
  }

  private Mono<MenuOrderEntity> findMenuOrderByMenuOptionId(Long menuOptionId) {
    return menuOrderRepository.findByMenuOptionId(menuOptionId)
        .map(Mono::just)
        .orElse(Mono.empty());
  }

}
