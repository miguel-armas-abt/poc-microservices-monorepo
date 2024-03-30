package com.demo.bbq.business.orderhub.application.service.tableplacement;

import com.demo.bbq.business.orderhub.application.dto.tableorder.request.MenuOrderRequestDTO;
import io.reactivex.Completable;
import java.util.List;

public interface TablePlacementService {

  Completable generateTableOrder(List<MenuOrderRequestDTO> requestedMenuOrderList, Integer tableNumber);
}
