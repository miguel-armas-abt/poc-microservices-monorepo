package com.demo.bbq.repository.product.config;

import com.demo.bbq.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.repository.product.ProductRepository;
import com.demo.bbq.utils.restclient.retrofit.HttpReactiveClient;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ProductRestClientConfig {

  private static final String SERVICE_NAME = "product-v1";
  private final ServiceConfigurationProperties properties;

  @Bean(SERVICE_NAME)
  ProductRepository create(OkHttpClient.Builder builder) {
    return HttpReactiveClient.builder()
        .clientBuilder(builder)
        .baseUrl(properties.searchEndpoint(SERVICE_NAME))
        .connectTimeout(Duration.ofMillis(300L))
        .readTimeout(Duration.ofMillis(1200L))
        .writeTimeout(Duration.ofMillis(700L))
        .buildProxy(ProductRepository.class);
  }
}
