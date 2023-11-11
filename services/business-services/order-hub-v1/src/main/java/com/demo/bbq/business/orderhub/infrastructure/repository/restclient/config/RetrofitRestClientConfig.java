package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.config;

import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.tableorder.TableOrderApi;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.InvoiceApi;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.properties.RestClientBaseUrlProperties;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.menuv1.retrofit.MenuV1Api;
import com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.menuv2.retrofit.MenuV2Api;
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
  MenuV1Api menuOptionV1Api(OkHttpClient.Builder builder) {
    return new Retrofit.Builder()
        .baseUrl(properties.getMenuV1BaseUrl())
        .client(client().build())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(MenuV1Api.class);
  }

  @Bean
  MenuV2Api menuOptionV2Api(OkHttpClient.Builder builder) {
    return SupportHttpClient.builder()
        .clientBuilder(builder)
        .baseUrl(properties.getMenuV2BaseUrl())
        .connectTimeout(Duration.ofMillis(300L))
        .readTimeout(Duration.ofMillis(1200L))
        .writeTimeout(Duration.ofMillis(700L))
        .buildProxy(MenuV2Api.class);
  }

  @Bean
  TableOrderApi diningRoomOrderApi(OkHttpClient.Builder builder) {
    return SupportHttpClient.builder()
        .clientBuilder(builder)
        .baseUrl(properties.getTableOrderBaseUrl())
        .connectTimeout(Duration.ofMillis(300L))
        .readTimeout(Duration.ofMillis(1200L))
        .writeTimeout(Duration.ofMillis(700L))
        .buildProxy(TableOrderApi.class);
  }

  @Bean
  InvoiceApi invoiceApi(OkHttpClient.Builder builder) {
    return SupportHttpClient.builder()
        .clientBuilder(builder)
        .baseUrl(properties.getInvoiceBaseUrl())
        .connectTimeout(Duration.ofMillis(300L))
        .readTimeout(Duration.ofMillis(1200L))
        .writeTimeout(Duration.ofMillis(700L))
        .buildProxy(InvoiceApi.class);
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
