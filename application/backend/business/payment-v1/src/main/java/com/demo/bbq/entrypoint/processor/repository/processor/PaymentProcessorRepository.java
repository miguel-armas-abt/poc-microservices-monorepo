package com.demo.bbq.entrypoint.processor.repository.processor;

import com.demo.bbq.commons.restclient.retrofit.ReactiveTransformer;
import com.demo.bbq.entrypoint.processor.repository.processor.wrapper.request.PaymentRequestWrapper;
import com.demo.bbq.entrypoint.processor.repository.processor.wrapper.response.PaymentResponseWrapper;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.*;

public interface PaymentProcessorRepository {

  @Streaming
  @POST("execute")
  @Headers("Accept: application/x-ndjson")
  Observable<ResponseBody> execute(@Body PaymentRequestWrapper request);

  default Observable<PaymentResponseWrapper> executeFacade(PaymentRequestWrapper request) {
    return execute(request).compose(ReactiveTransformer.of(PaymentResponseWrapper.class));
  }
}
