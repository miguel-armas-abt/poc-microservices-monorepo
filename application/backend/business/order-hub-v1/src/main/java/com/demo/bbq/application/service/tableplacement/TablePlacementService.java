package com.demo.bbq.application.service.tableplacement;

import com.demo.bbq.application.dto.tableorder.request.MenuOrderRequestDTO;
import io.reactivex.rxjava3.core.Completable;
import java.util.List;

public interface TablePlacementService {

  Completable generateTableOrder(List<MenuOrderRequestDTO> requestedMenuOrderList, Integer tableNumber);
}
