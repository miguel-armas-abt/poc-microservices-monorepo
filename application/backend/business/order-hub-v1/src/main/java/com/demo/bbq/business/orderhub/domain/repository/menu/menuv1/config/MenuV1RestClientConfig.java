package com.demo.bbq.business.orderhub.domain.repository.menu.menuv1.config;

import com.demo.bbq.business.orderhub.domain.repository.menu.menuv1.MenuV1Repository;
import com.demo.bbq.support.httpclient.retrofit.reactive.SupportHttpClient;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
@RequiredArgsConstructor
public class MenuV1RestClientConfig {

  private final MenuV1RestClientProperties properties;

//  @Bean
//  MenuV1Repository buildMenuV1Repository(OkHttpClient.Builder builder) {
//    return SupportHttpClient.builder()
//        .clientBuilder(builder)
//        .baseUrl(properties.getBaseURL())
//        .connectTimeout(Duration.ofMillis(300L))
//        .readTimeout(Duration.ofMillis(1200L))
//        .writeTimeout(Duration.ofMillis(700L))
//        .buildProxy(MenuV1Repository.class);
//  }

  @Bean
  MenuV1Repository buildMenuV1Repository(OkHttpClient.Builder builder) {
    return new Retrofit.Builder()
        .baseUrl(properties.getBaseURL())
        .client(builder.build())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(MenuV1Repository.class);
  }
}