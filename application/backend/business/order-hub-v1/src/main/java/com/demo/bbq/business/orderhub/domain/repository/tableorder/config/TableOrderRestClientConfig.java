package com.demo.bbq.business.orderhub.domain.repository.tableorder.config;

import com.demo.bbq.business.orderhub.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.business.orderhub.domain.repository.tableorder.TableOrderRepository;
import com.demo.bbq.utils.config.retrofit.HttpReactiveClient;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TableOrderRestClientConfig {

  private static final String SERVICE_NAME = "table-placement-v1";

  @Bean(SERVICE_NAME)
  TableOrderRepository create(OkHttpClient.Builder builder,
                              ServiceConfigurationProperties properties) {
    return HttpReactiveClient.builder()
        .clientBuilder(builder)
        .baseUrl(properties.searchEndpoint(SERVICE_NAME))
        .connectTimeout(Duration.ofMillis(300L))
        .readTimeout(Duration.ofMillis(1200L))
        .writeTimeout(Duration.ofMillis(700L))
        .buildProxy(TableOrderRepository.class);
  }
}