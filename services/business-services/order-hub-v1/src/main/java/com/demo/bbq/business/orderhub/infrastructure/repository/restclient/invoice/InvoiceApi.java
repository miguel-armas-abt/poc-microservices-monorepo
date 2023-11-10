package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.request.PaymentRequest;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.request.ProductRequestDto;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.response.ProformaInvoiceDto;
import io.reactivex.Single;
import java.util.List;
import okhttp3.ResponseBody;
import retrofit2.http.*;

public interface InvoiceApi {

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @POST("proformas")
  Single<ProformaInvoiceDto> generateProforma(@Body List<ProductRequestDto> productList);

  @Streaming
  @Headers({"Accept: application/stream+json"})
  @POST("payments")
  Single<ResponseBody> sendToPay(@Body PaymentRequest paymentRequest);
}
