package com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.invoice;

import com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.invoice.dto.request.PaymentRequest;
import com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.invoice.dto.request.ProductRequestDto;
import com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.invoice.dto.response.ProformaInvoiceDto;
import io.reactivex.Single;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.http.*;

public interface InvoiceApi {

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @POST("invoices/proformas")
  Single<ProformaInvoiceDto> generateProforma(@Body List<ProductRequestDto> productList);

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @POST("invoices/payments")
  Single<ResponseBody> sendToPay(@Body PaymentRequest paymentRequest);
}
