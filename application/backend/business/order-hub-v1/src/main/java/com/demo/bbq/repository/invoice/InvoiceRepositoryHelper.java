package com.demo.bbq.repository.invoice;

import static com.demo.bbq.repository.invoice.InvoiceConfig.INVOICE_SERVICE_NAME;

import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.repository.invoice.wrapper.request.PaymentRequestWrapper;
import com.demo.bbq.repository.invoice.wrapper.request.ProductRequestWrapper;
import com.demo.bbq.repository.invoice.wrapper.response.ProformaInvoiceResponseWrapper;
import io.reactivex.rxjava3.core.Single;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class InvoiceRepositoryHelper {

  private final ServiceConfigurationProperties properties;
  private final InvoiceRepository repository;

  public Single<ProformaInvoiceResponseWrapper> generateProforma(HttpServletRequest httpRequest,
                                                                 List<ProductRequestWrapper> productList) {
    return repository.generateProforma(getHeaders(httpRequest), productList);
  }

  public Single<ResponseBody> sendToPay(HttpServletRequest httpRequest, PaymentRequestWrapper paymentRequestWrapper) {
    return repository.sendToPay(getHeaders(httpRequest), paymentRequestWrapper);
  }

  private Map<String, String> getHeaders(HttpServletRequest httpRequest) {
    return properties.searchHeaders(httpRequest, INVOICE_SERVICE_NAME);
  }

}
