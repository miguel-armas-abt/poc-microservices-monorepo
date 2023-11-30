package com.demo.bbq.business.tableplacement.infrastructure.resource.rest;

import java.util.List;
import java.util.function.Consumer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import com.demo.bbq.business.tableplacement.application.service.TablePlacementService;
import com.demo.bbq.business.tableplacement.domain.model.request.MenuOrderRequest;
import com.demo.bbq.business.tableplacement.domain.model.response.TableOrder;
import com.demo.bbq.support.logstash.Markers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/bbq/business/table-placement/v1/table-orders")
public class TablePlacementRestServiceImpl implements TablePlacementRestService {

  private final TablePlacementService tablePlacementService;

  @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Mono<TableOrder> findByTableNumber(
      HttpServletRequest servletRequest, @RequestParam(value = "tableNumber") Integer tableNumber) {
    logRequest.accept(servletRequest);
    return tablePlacementService.findByTableNumber(tableNumber);
  }

  @DeleteMapping
  public Mono<Void> cleanTable(HttpServletRequest servletRequest,
                               HttpServletResponse servletResponse,
                               @RequestParam(value = "tableNumber") Integer tableNumber) {
    logRequest.accept(servletRequest);
    return tablePlacementService.cleanTable(tableNumber)
        .doOnSuccess(tableOrderId -> servletResponse.setStatus(204))
        .then(Mono.empty());
  }

  @PatchMapping
  public Mono<Void> generateTableOrder(HttpServletRequest servletRequest,
                                       HttpServletResponse servletResponse,
                                       @Valid @RequestBody List<MenuOrderRequest> requestedMenuOrderList,
                                       @RequestParam(value = "tableNumber") Integer tableNumber) {
    logRequest.accept(servletRequest);
    return tablePlacementService.generateTableOrder(requestedMenuOrderList, tableNumber)
        .doOnSuccess(tableOrderId -> servletResponse.setStatus(201))
        .then(Mono.empty());
  }

  private final static Consumer<HttpServletRequest> logRequest = servletRequest->
      log.info(Markers.SENSITIVE_JSON, "{}", servletRequest.getMethod() + ": " + servletRequest.getRequestURI());

}
