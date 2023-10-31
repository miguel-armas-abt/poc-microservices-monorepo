package com.demo.bbq.business.diningroomorder.application.service.impl;

import com.demo.bbq.business.diningroomorder.domain.model.dto.DiningRoomOrderDto;
import com.demo.bbq.business.diningroomorder.domain.model.dto.MenuOrderDto;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.database.impl.MenuOrderRepositoryReactive;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.database.impl.TableRepositoryReactive;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoption.MenuOptionApi;
import com.demo.bbq.business.diningroomorder.application.service.DiningRoomOrderService;
import com.demo.bbq.business.diningroomorder.domain.exception.DiningRoomOrderException;
import com.demo.bbq.business.diningroomorder.domain.model.request.MenuOrderRequest;
import com.demo.bbq.business.diningroomorder.infrastructure.mapper.DiningRoomOrderMapper;
import com.demo.bbq.business.diningroomorder.infrastructure.mapper.MenuOrderMapper;
import com.demo.bbq.support.exception.model.ApiException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import io.reactivex.Completable;
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
  public Mono<DiningRoomOrderDto> findByTableNumber(Integer tableNumber) {
    return tableRepository.findByTableNumber(tableNumber)
        .map(diningRoomOrderMapper::fromEntityToDto)
        .switchIfEmpty(Mono.error(DiningRoomOrderException.ERROR0000.buildException()));
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

  private DiningRoomOrderDto updateExistingDiningRoomOrder(DiningRoomOrderDto diningRoomOrder, List<MenuOrderRequest> requestedMenuOrderList) {
    Map<Long, MenuOrderDto> existingMenuOrderMap = diningRoomOrder.getMenuOrderList()
        .stream()
        .collect(Collectors.toMap(MenuOrderDto::getMenuOptionId, savedMenuOrder -> savedMenuOrder));

    requestedMenuOrderList.forEach(requestedMenuOrder -> {
        if (menuOptionAlreadyExist.test(existingMenuOrderMap, requestedMenuOrder.getMenuOptionId())) {
          increaseQuantity.accept(existingMenuOrderMap, requestedMenuOrder);
        } else {
          if(existMenuOption(requestedMenuOrder.getMenuOptionId())) {
            existingMenuOrderMap.put(requestedMenuOrder.getMenuOptionId(), menuOrderMapper.fromRequestToDto(requestedMenuOrder));
          }
        }
    });
    diningRoomOrder.setMenuOrderList(new ArrayList<>(existingMenuOrderMap.values()));
    return diningRoomOrder;
  }

  private final BiPredicate<Map<Long, MenuOrderDto>, Long> menuOptionAlreadyExist = Map::containsKey;

  private final BiConsumer<Map<Long, MenuOrderDto>, MenuOrderRequest> increaseQuantity = (existingMenuOrderMap, requestedMenuOrder) -> {
    MenuOrderDto existingMenuOrder = existingMenuOrderMap.get(requestedMenuOrder.getMenuOptionId());
    existingMenuOrder.setQuantity(existingMenuOrder.getQuantity() + requestedMenuOrder.getQuantity());
    existingMenuOrderMap.put(requestedMenuOrder.getMenuOptionId(), existingMenuOrder);
  };

  private Boolean existMenuOption(Long menuOptionId) {
    return menuOptionApi.findById(menuOptionId)
        .map(response -> Objects.nonNull(response.getId()))
        .block();
  }

  Mono<Throwable> validApiException(Throwable throwable, String exceptionCode) {
    return Mono.just(throwable)
        .filter(ApiException.class::isInstance)
        .filter(exception -> ((ApiException) exception).getErrorCode().equals(exceptionCode))
        .switchIfEmpty(Mono.error(throwable));
  }

}
