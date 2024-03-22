package com.demo.bbq.business.menu.domain.repository.restclient.product.config;

import com.demo.bbq.business.menu.domain.repository.restclient.product.ProductApi;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
@RequiredArgsConstructor
public class ProductApiConfig {

  private final ProductApiProperties properties;

  @Bean
  ProductApi productAPI(OkHttpClient.Builder builder) {
    return new Retrofit.Builder()
        .baseUrl(properties.getProductBaseUrl())
        .client(builder.build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(ProductApi.class);
  }
}
