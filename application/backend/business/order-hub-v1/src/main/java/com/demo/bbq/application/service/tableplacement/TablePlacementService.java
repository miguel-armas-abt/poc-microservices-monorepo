package com.demo.bbq.application.service.tableplacement;

import com.demo.bbq.application.dto.tableorder.request.MenuOrderRequestDTO;
import com.demo.bbq.repository.tableorder.wrapper.TableOrderRequestWrapper;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

public interface TablePlacementService {

  Completable generateTableOrder(HttpServletRequest httpRequest,
                                 List<MenuOrderRequestDTO> requestedMenuOrderList,
                                 Integer tableNumber);

  Single<TableOrderRequestWrapper> findByTableNumber(HttpServletRequest httpRequest,
                                                     Integer tableNumber);
}
