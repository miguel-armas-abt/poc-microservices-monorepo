package com.demo.bbq.business.orderhub.application.tableorder.service;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.tableorder.dto.MenuOrderRequestDto;
import io.reactivex.Completable;
import java.util.List;

public interface TableOrderService {

  Completable generateTableOrder(List<MenuOrderRequestDto> requestedMenuOrderList, Integer tableNumber);
}
