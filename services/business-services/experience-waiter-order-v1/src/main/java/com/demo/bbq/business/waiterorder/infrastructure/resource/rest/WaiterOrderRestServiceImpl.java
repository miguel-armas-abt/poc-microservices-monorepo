package com.demo.bbq.business.waiterorder.infrastructure.resource.rest;

import com.demo.bbq.business.waiterorder.application.service.WaiterOrderService;
import com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.diningroomorder.DiningRoomOrderApi;
import com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.diningroomorder.dto.DiningRoomOrderDto;
import com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.diningroomorder.dto.MenuOrderRequestDto;
import com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.invoice.InvoiceApi;
import com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.invoice.dto.request.PaymentRequest;
import com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.invoice.dto.request.ProductRequestDto;
import com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.invoice.dto.response.ProformaInvoiceDto;
import com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.menuoption.MenuOptionApi;
import com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.menuoption.dto.MenuOptionDto;
import com.demo.bbq.support.logstash.Markers;
import io.reactivex.Single;
import java.util.List;
import java.util.function.Consumer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.adapter.rxjava.RxJava2Adapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/bbq/experience/v1/waiter-orders")
public class WaiterOrderRestServiceImpl implements WaiterOrderRestService {

  private final WaiterOrderService waiterOrderService;
  private final MenuOptionApi menuOptionApi;
  private final DiningRoomOrderApi diningRoomOrderApi;

  private final InvoiceApi invoiceApi;

  @GetMapping(value = "/menu-options", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Flux<MenuOptionDto> findMenuByCategory(
      HttpServletRequest servletRequest, @RequestParam(value = "category") String categoryCode) {
    logRequest.accept(servletRequest);
    return menuOptionApi.findByCategory(categoryCode);
  }

  @PatchMapping("/dining-room-orders")
  public Mono<Void> generateTableOrder(HttpServletRequest servletRequest,
                                       HttpServletResponse servletResponse,
                                       @Valid @RequestBody List<MenuOrderRequestDto> requestedMenuOrderList,
                                       @RequestParam(value = "tableNumber") Integer tableNumber) {
    return RxJava2Adapter.singleToMono(diningRoomOrderApi.generateTableOrder(requestedMenuOrderList, tableNumber))
        .doOnSuccess(tableOrderId -> servletResponse.setStatus(201))
        .then(Mono.empty());
  }

  @GetMapping("/dining-room-orders")
  public Mono<DiningRoomOrderDto> findByTableNumber(HttpServletRequest servletRequest,
                                                    HttpServletResponse servletResponse,
                                                    @RequestParam(value = "tableNumber") Integer tableNumber) {
    return RxJava2Adapter.singleToMono(diningRoomOrderApi.findByTableNumber(tableNumber));
  }

  @PostMapping(value = "/proforma-invoices", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Single<ProformaInvoiceDto> generateProforma(HttpServletRequest servletRequest,
                                                     @Valid @RequestBody List<ProductRequestDto> productList) {
    logRequest.accept(servletRequest);
    return invoiceApi.generateProforma(productList);
  }

  @PostMapping("/invoice-payments")
  public Mono<Void> sendToPay(HttpServletRequest servletRequest, HttpServletResponse servletResponse,
                               @Valid @RequestBody PaymentRequest paymentRequest) {
    logRequest.accept(servletRequest);
    return RxJava2Adapter.singleToMono(invoiceApi.sendToPay(paymentRequest))
        .doOnSuccess(ignore -> servletResponse.setStatus(201))
        .then(Mono.empty());
  }

  private final static Consumer<HttpServletRequest> logRequest = servletRequest->
      log.info(Markers.SENSITIVE_JSON, "{}", servletRequest.getMethod() + ": " + servletRequest.getRequestURI());

}
