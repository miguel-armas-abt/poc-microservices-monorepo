package com.demo.bbq.application.service.tableplacement;

import com.demo.bbq.repository.menu.MenuRepositoryStrategy;
import com.demo.bbq.repository.menu.wrapper.response.MenuOptionResponseWrapper;
import com.demo.bbq.application.dto.tableorder.request.MenuOrderRequestDTO;
import com.demo.bbq.repository.tableorder.TableOrderRepositoryHelper;
import com.demo.bbq.repository.tableorder.wrapper.TableOrderRequestWrapper;
import com.demo.bbq.utils.errors.exceptions.BusinessException;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TablePlacementServiceImpl implements TablePlacementService {

  private final TableOrderRepositoryHelper tableOrderRepository;
  private final MenuRepositoryStrategy menuRepositoryStrategy;

  @Override
  public Completable generateTableOrder(HttpServletRequest httpRequest,
                                        List<MenuOrderRequestDTO> requestedMenuOrderList,
                                        Integer tableNumber) {

    return Observable.fromIterable(requestedMenuOrderList)
        .flatMapMaybe(menuOrder -> existsMenuOption(httpRequest, menuOrder))
        .ignoreElements()
        .andThen(tableOrderRepository.generateTableOrder(httpRequest, requestedMenuOrderList, tableNumber))
        .ignoreElement();
  }

  private Maybe<MenuOptionResponseWrapper> existsMenuOption(HttpServletRequest httpRequest, MenuOrderRequestDTO menuOrderRequest) {
    return menuRepositoryStrategy.getService().findByProductCode(httpRequest, menuOrderRequest.getProductCode())
        .switchIfEmpty(Maybe.error(new BusinessException("MenuOptionNotFound")));
  }

  @Override
  public Single<TableOrderRequestWrapper> findByTableNumber(HttpServletRequest httpRequest, Integer tableNumber) {
    return tableOrderRepository.findByTableNumber(httpRequest, tableNumber);
  }

}