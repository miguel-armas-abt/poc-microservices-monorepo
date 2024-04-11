package com.demo.bbq.business.invoice.domain.repository.product.config;

import com.demo.bbq.business.invoice.domain.repository.product.ProductRepository;
import com.demo.bbq.support.httpclient.retrofit.reactive.SupportHttpClient;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class ProductRestClientConfig {

  private final ProductRestClientProperties properties;

  @Bean
  ProductRepository buildProductRepository(OkHttpClient.Builder builder) {
    return SupportHttpClient.builder()
        .clientBuilder(builder)
        .baseUrl(properties.getBaseURL())
        .connectTimeout(Duration.ofMillis(300L))
        .readTimeout(Duration.ofMillis(1200L))
        .writeTimeout(Duration.ofMillis(700L))
        .buildProxy(ProductRepository.class);
  }

}
