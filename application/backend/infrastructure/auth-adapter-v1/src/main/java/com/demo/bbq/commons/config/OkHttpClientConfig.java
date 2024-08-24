package com.demo.bbq.commons.config;

import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.commons.errors.handler.external.strategy.RestClientErrorStrategy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class OkHttpClientConfig {

  @Bean
  OkHttpClient.Builder client(List<RestClientErrorStrategy> strategies,
                              ApplicationProperties properties) {

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
    return new OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(new ExchangeInterceptor(strategies, properties));
  }
}