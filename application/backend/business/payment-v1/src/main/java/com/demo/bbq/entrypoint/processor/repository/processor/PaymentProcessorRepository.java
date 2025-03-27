package com.demo.bbq.entrypoint.processor.repository.processor;

import com.demo.bbq.commons.custom.properties.ApplicationProperties;
import com.demo.bbq.commons.core.restclient.RetrofitFactory;
import com.demo.bbq.commons.core.restclient.StreamingTransformer;
import com.demo.bbq.entrypoint.processor.repository.processor.wrapper.request.PaymentRequestWrapper;
import com.demo.bbq.entrypoint.processor.repository.processor.wrapper.response.PaymentResponseWrapper;
import io.reactivex.rxjava3.core.Observable;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.http.*;

public interface PaymentProcessorRepository {

  String MENU_V2_SERVICE_NAME = "payment-processor";

  @Streaming
  @POST("execute")
  @Headers("Accept: application/x-ndjson")
  Observable<ResponseBody> executeBase(@Body PaymentRequestWrapper request);

  default Observable<PaymentResponseWrapper> execute(PaymentRequestWrapper request) {
    return executeBase(request).compose(StreamingTransformer.of(PaymentResponseWrapper.class));
  }

  @Configuration
  class PaymentProcessorRepositoryConfig {

    @Bean(MENU_V2_SERVICE_NAME)
    PaymentProcessorRepository create(OkHttpClient.Builder builder, ApplicationProperties properties) {
      return RetrofitFactory.create(builder, properties, MENU_V2_SERVICE_NAME, PaymentProcessorRepository.class);
    }
  }
}