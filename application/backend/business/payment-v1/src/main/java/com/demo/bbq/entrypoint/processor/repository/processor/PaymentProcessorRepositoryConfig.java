package com.demo.bbq.entrypoint.processor.repository.processor;

import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.commons.restclient.enums.TimeoutLevel;
import com.demo.bbq.commons.restclient.retrofit.HttpReactiveClient;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentProcessorRepositoryConfig {

  public static final String MENU_V2_SERVICE_NAME = "payment-processor";

  @Bean(MENU_V2_SERVICE_NAME)
  PaymentProcessorRepository create(OkHttpClient.Builder builder, ApplicationProperties properties) {
    TimeoutLevel timeoutLevel = properties.searchPerformance(MENU_V2_SERVICE_NAME).getTimeout();

    return HttpReactiveClient.builder()
        .clientBuilder(builder)
        .baseUrl(properties.searchEndpoint(MENU_V2_SERVICE_NAME))
        .connectTimeout(timeoutLevel.getConnectionTimeoutDuration())
        .writeTimeout(timeoutLevel.getWriteTimeoutDuration())
        .readTimeout(timeoutLevel.getReadTimeoutDuration())
        .buildProxy(PaymentProcessorRepository.class);
  }
}