package com.demo.bbq.business.orderhub.domain.repository.menu.menuv2.config;

import com.demo.bbq.business.orderhub.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.business.orderhub.domain.repository.menu.menuv2.MenuV2Repository;
import com.demo.bbq.utils.config.retrofit.HttpReactiveClient;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MenuV2RestClientConfig {

  private static final String SERVICE_NAME = "menu-v2";

  @Bean(SERVICE_NAME)
  MenuV2Repository create(OkHttpClient.Builder builder,
                          ServiceConfigurationProperties properties) {
    return HttpReactiveClient.builder()
        .clientBuilder(builder)
        .baseUrl(properties.searchEndpoint(SERVICE_NAME))
        .connectTimeout(Duration.ofMillis(300L))
        .readTimeout(Duration.ofMillis(1200L))
        .writeTimeout(Duration.ofMillis(700L))
        .buildProxy(MenuV2Repository.class);
  }
}