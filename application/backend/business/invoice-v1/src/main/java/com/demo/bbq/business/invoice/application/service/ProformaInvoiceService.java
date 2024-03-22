package com.demo.bbq.business.invoice.application.service;

import com.demo.bbq.business.invoice.application.dto.request.ProductRequest;
import com.demo.bbq.business.invoice.application.dto.response.ProformaInvoiceResponse;
import io.reactivex.Single;
import java.util.List;

public interface ProformaInvoiceService {

  Single<ProformaInvoiceResponse> generateProformaInvoice(List<ProductRequest> products);

}
