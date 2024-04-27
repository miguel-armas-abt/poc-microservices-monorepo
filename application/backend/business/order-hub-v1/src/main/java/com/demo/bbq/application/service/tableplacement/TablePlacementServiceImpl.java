package com.demo.bbq.application.service.tableplacement;

import com.demo.bbq.repository.menu.MenuRepositoryHelper;
import com.demo.bbq.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.repository.tableorder.TableOrderRepository;
import com.demo.bbq.application.dto.tableorder.request.MenuOrderRequestDTO;
import com.demo.bbq.utils.errors.exceptions.BusinessException;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TablePlacementServiceImpl implements TablePlacementService {

  private final TableOrderRepository tableOrderRepository;
  private final MenuRepositoryHelper menuRepositoryHelper;

  @Override
  public Completable generateTableOrder(List<MenuOrderRequestDTO> requestedMenuOrderList, Integer tableNumber) {
    return Observable.fromIterable(requestedMenuOrderList)
        .flatMapMaybe(this::existsMenuOption)
        .ignoreElements()
        .andThen(tableOrderRepository.generateTableOrder(requestedMenuOrderList, tableNumber))
        .ignoreElement();
  }

  private Maybe<MenuOptionResponseWrapper> existsMenuOption(MenuOrderRequestDTO menuOrderRequest) {
    return menuRepositoryHelper.getService().findByProductCode(menuOrderRequest.getProductCode())
        .switchIfEmpty(Maybe.error(new BusinessException("MenuOptionNotFound")));
  }

}