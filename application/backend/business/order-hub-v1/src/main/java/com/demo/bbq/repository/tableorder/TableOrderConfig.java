package com.demo.bbq.repository.tableorder;

import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.restclient.retrofit.HttpReactiveClient;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TableOrderConfig {

  public static final String TABLE_PLACEMENT_SERVICE_NAME = "table-placement-v1";

  @Bean(TABLE_PLACEMENT_SERVICE_NAME)
  TableOrderRepository create(OkHttpClient.Builder builder,
                              ServiceConfigurationProperties properties) {
    return HttpReactiveClient.builder()
        .clientBuilder(builder)
        .baseUrl(properties.searchEndpoint(TABLE_PLACEMENT_SERVICE_NAME))
        .connectTimeout(Duration.ofMillis(300L))
        .readTimeout(Duration.ofMillis(1200L))
        .writeTimeout(Duration.ofMillis(700L))
        .buildProxy(TableOrderRepository.class);
  }
}