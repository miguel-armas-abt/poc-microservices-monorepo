package com.demo.bbq.business.invoice.application.service.impl;

import com.demo.bbq.business.invoice.infrastructure.properties.InvoiceProperties;
import com.demo.bbq.business.invoice.infrastructure.repository.restclient.DiningRoomOrderApi;
import com.demo.bbq.business.invoice.infrastructure.repository.restclient.MenuOptionV2Api;
import com.demo.bbq.business.invoice.application.service.ProformaInvoiceService;
import com.demo.bbq.business.invoice.domain.model.response.Invoice;
import com.demo.bbq.business.invoice.domain.model.response.MenuOrder;
import com.demo.bbq.business.invoice.infrastructure.repository.restclient.dto.menuoption.MenuOptionDto;
import io.reactivex.Observable;
import io.reactivex.Single;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProformaInvoiceServiceImpl implements ProformaInvoiceService {

  private final DiningRoomOrderApi diningRoomOrderApi;
  private final MenuOptionV2Api menuOptionV2Api;

  private final InvoiceProperties properties;

  @Override
  public Single<Invoice> generateProformaInvoice(Integer tableNumber) {
    List<MenuOrder> menuOrderList = new ArrayList<>();
    AtomicReference<BigDecimal> subtotalInvoice = new AtomicReference<>(BigDecimal.ZERO);
    return diningRoomOrderApi.findByTableNumber(tableNumber)
        .flatMapObservable(diningRoomOrder -> Observable.fromIterable(diningRoomOrder.getMenuOrderList()))
        .flatMapSingle(menuOrder -> menuOptionV2Api.findById(menuOrder.getMenuOptionId())
            .doOnSuccess(menuOptionFound -> {
              BigDecimal totalMenu = menuOptionFound.getPrice().multiply(new BigDecimal(menuOrder.getQuantity()));
              BigDecimal actualAmount = subtotalInvoice.get();
              subtotalInvoice.set(actualAmount.add(totalMenu));
              menuOrderList.add(toMenuOrder(menuOptionFound, menuOrder.getQuantity(), totalMenu));
            })
        )
        .ignoreElements()
        .andThen(Single.defer(() -> Single.just(toInvoice(menuOrderList, subtotalInvoice.get())))); // defer will makes sure each subscriber can get its own source sequence, independent of the other subscribers
  }

  private static MenuOrder toMenuOrder(MenuOptionDto menuOption, Integer quantity, BigDecimal totalMenu) {
    return MenuOrder.builder()
        .price(menuOption.getPrice())
        .quantity(quantity)
        .description(menuOption.getDescription())
        .total(totalMenu)
        .build();
  }

  private Invoice toInvoice(List<MenuOrder> menuOrderList, BigDecimal subtotal) {
    BigDecimal igv = properties.getIgv();
    return Invoice.builder()
        .menuOrderList(menuOrderList)
        .subtotal(subtotal)
        .igv(igv)
        .total(subtotal.add(subtotal.multiply(igv)))
        .build();
  }

}
