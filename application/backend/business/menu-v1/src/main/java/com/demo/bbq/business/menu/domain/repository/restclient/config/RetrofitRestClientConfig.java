package com.demo.bbq.business.menu.domain.repository.restclient.config;

import com.demo.bbq.business.menu.domain.repository.restclient.ProductApi;
import com.demo.bbq.business.menu.domain.repository.restclient.properties.RestClientProperties;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
@RequiredArgsConstructor
public class RetrofitRestClientConfig {

  private final RestClientProperties properties;

  @Bean
  ProductApi productAPI(OkHttpClient.Builder builder) {
    return new Retrofit.Builder()
        .baseUrl(properties.getProductBaseUrl())
        .client(client().build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(ProductApi.class);
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
