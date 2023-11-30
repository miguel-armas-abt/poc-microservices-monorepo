package com.demo.bbq.business.invoice.application.service;

import com.demo.bbq.business.invoice.domain.model.request.ProductRequest;
import com.demo.bbq.business.invoice.domain.model.response.ProformaInvoice;
import io.reactivex.Single;
import java.util.List;

public interface ProformaInvoiceService {

  Single<ProformaInvoice> generateProformaInvoice(List<ProductRequest> products);

}
