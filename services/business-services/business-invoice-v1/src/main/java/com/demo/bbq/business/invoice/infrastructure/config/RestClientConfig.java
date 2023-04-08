package com.demo.bbq.business.invoice.infrastructure.config;

import com.demo.bbq.business.invoice.infrastructure.repository.restclient.DiningRoomOrderApi;
import com.demo.bbq.business.invoice.infrastructure.repository.restclient.MenuOptionV2Api;
import com.demo.bbq.support.reactive.httpclient.SupportHttpClient;
import java.time.Duration;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class RestClientConfig {

  @Value("${application.http-client.menu-option-v2.base-url}")
  private String menuOptionV2BaseUrl;

  @Value("${application.http-client.dining-room-order-v1.base-url}")
  private String diningRoomOrdersBaseUrl;

  @Bean
  MenuOptionV2Api menuOptionV2Api(OkHttpClient.Builder builder) {
    return SupportHttpClient.builder()
        .clientBuilder(builder)
        .baseUrl(this.menuOptionV2BaseUrl)
        .connectTimeout(Duration.ofMillis(300L))
        .readTimeout(Duration.ofMillis(1200L))
        .writeTimeout(Duration.ofMillis(700L))
        .buildProxy(MenuOptionV2Api.class);
  }

  @Bean
  DiningRoomOrderApi diningRoomOrderApi(OkHttpClient.Builder builder) {
    return new Retrofit.Builder()
        .baseUrl(diningRoomOrdersBaseUrl)
        .client(client().build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(DiningRoomOrderApi.class);
  }

  @Bean
  OkHttpClient.Builder client() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
    return new OkHttpClient.Builder().addInterceptor(interceptor);
  }
}