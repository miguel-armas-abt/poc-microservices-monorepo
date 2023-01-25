package com.demo.bbq.business.invoice.application.service;

import com.demo.bbq.business.invoice.domain.model.request.InvoiceRequest;
import com.demo.bbq.business.invoice.domain.model.response.Invoice;
import io.reactivex.Completable;
import io.reactivex.Single;

/**
 * <br/>Interface Service que define los métodos necesarios para tramitar la lógica de negocio del contexto Invoice.<br/>
 *
 * @author Miguel Armas Abt <br/>
 */
public interface InvoiceService {

  Single<Invoice> generateInvoice(Integer tableNumber);

  Completable sendToPay(InvoiceRequest invoiceRequest);

}
