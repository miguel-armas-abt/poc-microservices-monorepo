package com.demo.bbq.business.diningroomorder.infrastructure.config;

import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoptionv1.MenuOptionV1RetrofitApi;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoptionv2.MenuOptionV2RetrofitApi;
import com.demo.bbq.support.reactive.httpclient.SupportHttpClient;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.time.Duration;

@Configuration
public class RestClientConfig {

  @Value("${application.http-client.menu-option-v1.base-url}")
  private String menuOptionV1BaseUrl;

  @Value("${application.http-client.menu-option-v2.base-url}")
  private String menuOptionV2BaseUrl;

  @Bean
  MenuOptionV1RetrofitApi menuOptionV1Api(OkHttpClient.Builder builder) {
    return new Retrofit.Builder()
        .baseUrl(menuOptionV1BaseUrl)
        .client(client().build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(MenuOptionV1RetrofitApi.class);
  }

  @Bean
  MenuOptionV2RetrofitApi menuOptionV2Api(OkHttpClient.Builder builder) {
    return SupportHttpClient.builder()
        .clientBuilder(builder)
        .baseUrl(this.menuOptionV2BaseUrl)
        .connectTimeout(Duration.ofMillis(300L))
        .readTimeout(Duration.ofMillis(1200L))
        .writeTimeout(Duration.ofMillis(700L))
        .buildProxy(MenuOptionV2RetrofitApi.class);
  }

  @Bean
  OkHttpClient.Builder client() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
    return new OkHttpClient.Builder().addInterceptor(interceptor);
  }
}