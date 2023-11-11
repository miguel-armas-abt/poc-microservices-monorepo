package com.demo.bbq.business.orderhub.infrastructure.resource.rest.tableorder;

import com.demo.bbq.business.orderhub.application.tableorder.service.TableOrderService;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.tableorder.TableOrderApi;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.tableorder.dto.MenuOrderRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.tableorder.dto.TableOrderDto;
import com.demo.bbq.business.orderhub.infrastructure.resource.rest.OrderHubRestService;
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
public class TableOrderRestServiceImpl extends OrderHubRestService implements TableOrderRestService  {

  private final TableOrderService tableOrderService;
  private final TableOrderApi tableOrderApi;

  @PatchMapping("/table-orders")
  public Completable generateTableOrder(HttpServletRequest servletRequest,
                                        HttpServletResponse servletResponse,
                                        @Valid @RequestBody List<MenuOrderRequestDto> requestedMenuOrderList,
                                        @RequestParam(value = "tableNumber") Integer tableNumber) {
    return tableOrderService.generateTableOrder(requestedMenuOrderList, tableNumber)
        .doOnComplete(() -> servletResponse.setStatus(201));
  }

  @GetMapping("/table-orders")
  public Single<TableOrderDto> findByTableNumber(HttpServletRequest servletRequest,
                                                 HttpServletResponse servletResponse,
                                                 @RequestParam(value = "tableNumber") Integer tableNumber) {
    return tableOrderApi.findByTableNumber(tableNumber);
  }
}
