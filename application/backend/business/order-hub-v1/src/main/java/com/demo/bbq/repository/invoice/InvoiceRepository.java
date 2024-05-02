package com.demo.bbq.repository.invoice;

import com.demo.bbq.repository.invoice.wrapper.request.PaymentRequestWrapper;
import com.demo.bbq.repository.invoice.wrapper.request.ProductRequestWrapper;
import com.demo.bbq.repository.invoice.wrapper.response.ProformaInvoiceResponseWrapper;
import io.reactivex.rxjava3.core.Single;
import java.util.List;
import java.util.Map;
import okhttp3.ResponseBody;
import retrofit2.http.*;

public interface InvoiceRepository {

  @Streaming
  @POST("proformas")
  Single<ProformaInvoiceResponseWrapper> generateProforma(@HeaderMap Map<String, String> headers,
                                                          @Body List<ProductRequestWrapper> productList);

  @Streaming
  @POST("send-to-pay")
  Single<ResponseBody> sendToPay(@HeaderMap Map<String, String> headers,
                                 @Body PaymentRequestWrapper paymentRequestWrapper);
}
