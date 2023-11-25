package com.demo.bbq.business.invoice.infrastructure.repository.restclient.config;

import com.demo.bbq.business.invoice.infrastructure.repository.restclient.CoreProductApi;
import com.demo.bbq.business.invoice.infrastructure.repository.restclient.properties.RestClientBaseUrlProperties;
import com.demo.bbq.support.httpclient.retrofit.reactive.SupportHttpClient;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RetrofitRestClientConfig {

  private final RestClientBaseUrlProperties properties;

  @Bean
  CoreProductApi diningRoomOrderApi(OkHttpClient.Builder builder) {
    return SupportHttpClient.builder()
        .clientBuilder(builder)
        .baseUrl(properties.getProductBaseUrl())
        .connectTimeout(Duration.ofMillis(300L))
        .readTimeout(Duration.ofMillis(1200L))
        .writeTimeout(Duration.ofMillis(700L))
        .buildProxy(CoreProductApi.class);
  }

  @Bean
  OkHttpClient.Builder client() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
    return new OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(new ApiExceptionInterceptor());
  }
}
