package com.demo.bbq.business.diningroomorder.application.service.impl;

import com.demo.bbq.business.diningroomorder.domain.model.response.DiningRoomOrder;
import com.demo.bbq.business.diningroomorder.domain.model.response.MenuOrder;
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
  public Mono<Void> generateTableOrder(List<MenuOrderRequest> menuOrderRequestList, Integer tableNumber) {
    return findByTableNumber(tableNumber)
        .map(diningRoomOrder -> updateExistingDiningRoomOrder(diningRoomOrder, menuOrderRequestList))
        .flatMapMany(diningRoomOrder -> Flux.fromIterable(diningRoomOrder.getMenuOrderList())
            .flatMap(menuOrder -> menuOrderRepository.updateMenuOrder(menuOrderMapper.fromDomainToEntity(menuOrder, diningRoomOrder.getId()))))
        .ignoreElements()
        .flatMap(ignored -> Mono.empty());
  }

  private DiningRoomOrder updateExistingDiningRoomOrder(DiningRoomOrder diningRoomOrder, List<MenuOrderRequest> menuOrderRequestList) {
    Map<Long, MenuOrder> existingMenuOrderMap = diningRoomOrder.getMenuOrderList()
        .stream()
        .collect(Collectors.toMap(MenuOrder::getMenuOptionId, savedMenuOrder -> savedMenuOrder));

    menuOrderRequestList.forEach(requestedMenuOrder -> {
        if (menuOptionAlreadyExist.test(existingMenuOrderMap, requestedMenuOrder.getMenuOptionId())) {
          increaseQuantity.accept(existingMenuOrderMap, requestedMenuOrder);
        } else {
          if(existMenuOption(requestedMenuOrder.getMenuOptionId())) {
            existingMenuOrderMap.put(requestedMenuOrder.getMenuOptionId(), menuOrderMapper.fromRequestToDomain(requestedMenuOrder));
          }
        }
    });
    diningRoomOrder.setMenuOrderList(new ArrayList<>(existingMenuOrderMap.values()));
    return diningRoomOrder;
  }

  private final BiPredicate<Map<Long, MenuOrder>, Long> menuOptionAlreadyExist = Map::containsKey;

  private final BiConsumer<Map<Long, MenuOrder>, MenuOrderRequest> increaseQuantity = (existingMenuOrderMap, requestedMenuOrder) -> {
    MenuOrder existingMenuOrder = existingMenuOrderMap.get(requestedMenuOrder.getMenuOptionId());
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
