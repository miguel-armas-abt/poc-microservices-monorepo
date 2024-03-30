package com.demo.bbq.business.orderhub.domain.repository.tableorder.config;

import com.demo.bbq.business.orderhub.domain.repository.tableorder.TableOrderRepository;
import com.demo.bbq.support.httpclient.retrofit.reactive.SupportHttpClient;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TableOrderRestClientConfig {

  private final TableOrderRestClientProperties properties;

  @Bean
  TableOrderRepository diningRoomOrderApi(OkHttpClient.Builder builder) {
    return SupportHttpClient.builder()
        .clientBuilder(builder)
        .baseUrl(properties.getBaseURL())
        .connectTimeout(Duration.ofMillis(300L))
        .readTimeout(Duration.ofMillis(1200L))
        .writeTimeout(Duration.ofMillis(700L))
        .buildProxy(TableOrderRepository.class);
  }
}
