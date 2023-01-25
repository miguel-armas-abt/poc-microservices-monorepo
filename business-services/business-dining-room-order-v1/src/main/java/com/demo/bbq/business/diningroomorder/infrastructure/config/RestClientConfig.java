package com.demo.bbq.business.diningroomorder.infrastructure.config;

import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.MenuOptionRepository;
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

  @Value("${application.http-client.menu-options.base-url}")
  private String menuOptionsBaseUrl;

  @Bean
  MenuOptionRepository menuOptionApi(OkHttpClient.Builder builder) {
    return new Retrofit.Builder()
        .baseUrl(menuOptionsBaseUrl)
        .client(client().build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(MenuOptionRepository.class);
  }

  @Bean
  OkHttpClient.Builder client() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
    return new OkHttpClient.Builder().addInterceptor(interceptor);
  }
}