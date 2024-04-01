package com.demo.bbq.business.orderhub.domain.repository.invoice;

import com.demo.bbq.business.orderhub.domain.repository.invoice.wrapper.request.PaymentRequestWrapper;
import com.demo.bbq.business.orderhub.domain.repository.invoice.wrapper.request.ProductRequestWrapper;
import com.demo.bbq.business.orderhub.domain.repository.invoice.wrapper.response.ProformaInvoiceResponseWrapper;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.http.*;

public interface InvoiceRepository {

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @POST("proformas")
  Single<ProformaInvoiceResponseWrapper> generateProforma(@Body List<ProductRequestWrapper> productList);

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @POST("payments")
  Single<ResponseBody> sendToPay(@Body PaymentRequestWrapper paymentRequestWrapper);
}
