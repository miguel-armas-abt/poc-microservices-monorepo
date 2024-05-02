package com.demo.bbq.repository.menu.menuv2;

import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.restclient.retrofit.HttpReactiveClient;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MenuV2Config {

  public static final String MENU_V2_SERVICE_NAME = "menu-v2";

  @Bean(MENU_V2_SERVICE_NAME)
  MenuV2Repository create(OkHttpClient.Builder builder,
                          ServiceConfigurationProperties properties) {
    return HttpReactiveClient.builder()
        .clientBuilder(builder)
        .baseUrl(properties.searchEndpoint(MENU_V2_SERVICE_NAME))
        .connectTimeout(Duration.ofMillis(300L))
        .readTimeout(Duration.ofMillis(1200L))
        .writeTimeout(Duration.ofMillis(700L))
        .buildProxy(MenuV2Repository.class);
  }
}