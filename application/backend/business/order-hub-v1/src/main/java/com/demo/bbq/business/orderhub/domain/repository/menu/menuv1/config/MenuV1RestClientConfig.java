package com.demo.bbq.business.orderhub.domain.repository.menu.menuv1.config;

import com.demo.bbq.business.orderhub.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.business.orderhub.domain.repository.menu.menuv1.MenuV1Repository;
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

  private static final String SERVICE_NAME = "menu-v1";

  @Bean(SERVICE_NAME)
  MenuV1Repository create(OkHttpClient.Builder builder,
                          ServiceConfigurationProperties properties) {
    return new Retrofit.Builder()
        .baseUrl(properties.searchEndpoint(SERVICE_NAME))
        .client(builder.build())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(MenuV1Repository.class);
  }
}