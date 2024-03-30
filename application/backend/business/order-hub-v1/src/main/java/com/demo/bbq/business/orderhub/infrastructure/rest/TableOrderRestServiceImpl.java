package com.demo.bbq.business.orderhub.infrastructure.rest;

import com.demo.bbq.business.orderhub.application.service.tableplacement.TablePlacementService;
import com.demo.bbq.business.orderhub.domain.repository.tableorder.TableOrderRepository;
import com.demo.bbq.business.orderhub.application.dto.tableorder.request.MenuOrderRequestDTO;
import com.demo.bbq.business.orderhub.domain.repository.tableorder.wrapper.TableOrderRequestWrapper;
import io.reactivex.Completable;
import io.reactivex.Single;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Component
@RequiredArgsConstructor
public class TableOrderRestServiceImpl extends OrderHubRestService {

  private final TablePlacementService tablePlacementService;
  private final TableOrderRepository tableOrderRepository;

  @PatchMapping("/table-orders")
  public Completable generateTableOrder(HttpServletRequest servletRequest,
                                        HttpServletResponse servletResponse,
                                        @Valid @RequestBody List<MenuOrderRequestDTO> requestedMenuOrderList,
                                        @RequestParam(value = "tableNumber") Integer tableNumber) {
    return tablePlacementService.generateTableOrder(requestedMenuOrderList, tableNumber)
        .doOnComplete(() -> servletResponse.setStatus(201));
  }

  @GetMapping("/table-orders")
  public Single<TableOrderRequestWrapper> findByTableNumber(HttpServletRequest servletRequest,
                                                            HttpServletResponse servletResponse,
                                                            @RequestParam(value = "tableNumber") Integer tableNumber) {
    return tableOrderRepository.findByTableNumber(tableNumber);
  }
}
