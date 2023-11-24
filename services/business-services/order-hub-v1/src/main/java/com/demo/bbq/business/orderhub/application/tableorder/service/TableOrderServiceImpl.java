package com.demo.bbq.business.orderhub.application.tableorder.service;

import com.demo.bbq.business.orderhub.domain.exception.OrderHubException;
import com.demo.bbq.business.orderhub.infrastructure.repository.handler.menu.MenuRepositoryHandler;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto.MenuOptionDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.tableorder.TableOrderApi;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.tableorder.dto.MenuOrderRequestDto;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TableOrderServiceImpl implements TableOrderService {

  private final TableOrderApi tableOrderApi;
  private final MenuRepositoryHandler menuRepositoryHandler;

  @Override
  public Completable generateTableOrder(List<MenuOrderRequestDto> requestedMenuOrderList, Integer tableNumber) {
    return Observable.fromIterable(requestedMenuOrderList)
        .flatMapMaybe(this::existsMenuOption)
        .ignoreElements()
        .andThen(tableOrderApi.generateTableOrder(requestedMenuOrderList, tableNumber))
        .ignoreElement();
  }

  private Maybe<MenuOptionDto> existsMenuOption(MenuOrderRequestDto menuOrderRequest) {
    return menuRepositoryHandler.findByProductCode(menuOrderRequest.getProductCode())
        .switchIfEmpty(Maybe.error(OrderHubException.ERROR0001.buildException()));
  }

}
