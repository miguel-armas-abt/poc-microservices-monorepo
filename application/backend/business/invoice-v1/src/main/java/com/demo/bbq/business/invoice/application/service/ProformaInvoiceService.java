package com.demo.bbq.business.invoice.application.service;

import com.demo.bbq.business.invoice.application.dto.proformainvoice.request.ProductRequestDTO;
import com.demo.bbq.business.invoice.application.dto.proformainvoice.response.ProformaInvoiceResponseDTO;
import io.reactivex.rxjava3.core.Single;
import java.util.List;

public interface ProformaInvoiceService {

  Single<ProformaInvoiceResponseDTO> generateProformaInvoice(List<ProductRequestDTO> products);

}
