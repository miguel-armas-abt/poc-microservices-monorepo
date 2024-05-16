package com.demo.bbq.config.httpclient;

import com.demo.bbq.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.config.interceptor.ExchangeInterceptor;
import com.demo.bbq.utils.errors.handler.external.strategy.RestClientErrorStrategy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class HttpClientConfig {

  @Bean
  OkHttpClient.Builder client(List<RestClientErrorStrategy> strategies,
                              ServiceConfigurationProperties properties) {

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
    return new OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(new ExchangeInterceptor(strategies, properties));
  }
}