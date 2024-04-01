package com.demo.bbq.business.orderhub.application.service.invoices;

import com.demo.bbq.business.orderhub.application.dto.invoices.InvoicePaymentRequestDTO;
import com.demo.bbq.business.orderhub.domain.repository.invoice.wrapper.request.ProductRequestWrapper;
import com.demo.bbq.business.orderhub.domain.repository.invoice.wrapper.response.ProformaInvoiceResponseWrapper;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public interface InvoiceService {

  Single<ProformaInvoiceResponseWrapper> generateProforma(List<ProductRequestWrapper> productList);

  Completable sendToPay(InvoicePaymentRequestDTO invoicePaymentRequestDTO);
}
