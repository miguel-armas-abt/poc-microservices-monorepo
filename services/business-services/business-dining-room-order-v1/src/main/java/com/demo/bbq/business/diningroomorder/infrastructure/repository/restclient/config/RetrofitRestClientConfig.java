package com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.config;

import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.properties.RestClientBaseUrlProperties;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoption.menuoptionv1.retrofit.MenuOptionV1Api;
import com.demo.bbq.business.diningroomorder.infrastructure.repository.restclient.menuoption.menuoptionv2.retrofit.MenuOptionV2Api;
import com.demo.bbq.support.reactive.httpclient.SupportHttpClient;
import java.time.Duration;
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

  private final RestClientBaseUrlProperties properties;

  @Bean
  MenuOptionV1Api menuOptionV1Api(OkHttpClient.Builder builder) {
    return new Retrofit.Builder()
        .baseUrl(properties.getMenuOptionV1BaseUrl())
        .client(client().build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(MenuOptionV1Api.class);
  }

  @Bean
  MenuOptionV2Api menuOptionV2Api(OkHttpClient.Builder builder) {
    return SupportHttpClient.builder()
        .clientBuilder(builder)
        .baseUrl(properties.getMenuOptionV2BaseUrl())
        .connectTimeout(Duration.ofMillis(300L))
        .readTimeout(Duration.ofMillis(1200L))
        .writeTimeout(Duration.ofMillis(700L))
        .buildProxy(MenuOptionV2Api.class);
  }

  @Bean
  OkHttpClient.Builder client() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
    return new OkHttpClient.Builder().addInterceptor(interceptor);
  }
}
