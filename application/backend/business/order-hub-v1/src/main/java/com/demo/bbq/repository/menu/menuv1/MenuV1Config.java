package com.demo.bbq.repository.menu.menuv1;

import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
@RequiredArgsConstructor
public class MenuV1Config {

  public static final String MENU_V1_SERVICE_NAME = "menu-v1";

  @Bean(MENU_V1_SERVICE_NAME)
  MenuV1Repository create(OkHttpClient.Builder builder,
                          ServiceConfigurationProperties properties) {
    return new Retrofit.Builder()
        .baseUrl(properties.searchEndpoint(MENU_V1_SERVICE_NAME))
        .client(builder.build())
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .addConverterFactory(JacksonConverterFactory.create())
        .build()
        .create(MenuV1Repository.class);
  }
}