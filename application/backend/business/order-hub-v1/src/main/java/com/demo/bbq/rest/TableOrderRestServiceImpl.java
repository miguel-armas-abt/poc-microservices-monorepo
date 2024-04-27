package com.demo.bbq.rest;

import com.demo.bbq.application.service.tableplacement.TablePlacementService;
import com.demo.bbq.repository.tableorder.TableOrderRepository;
import com.demo.bbq.application.dto.tableorder.request.MenuOrderRequestDTO;
import com.demo.bbq.repository.tableorder.wrapper.TableOrderRequestWrapper;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
