package com.demo.bbq.business.invoice.application.service;

import com.demo.bbq.business.invoice.domain.model.response.Invoice;
import io.reactivex.Single;

public interface ProformaInvoiceService {

  Single<Invoice> generateProformaInvoice(Integer tableNumber);

}
