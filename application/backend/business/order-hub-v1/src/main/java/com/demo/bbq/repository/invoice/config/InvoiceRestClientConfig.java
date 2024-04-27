package com.demo.bbq.repository.invoice.config;

import com.demo.bbq.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.repository.invoice.InvoiceRepository;
import java.time.Duration;

import com.demo.bbq.utils.restclient.retrofit.HttpReactiveClient;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class InvoiceRestClientConfig {

  private static final String SERVICE_NAME = "invoice-v1";

  @Bean(SERVICE_NAME)
  InvoiceRepository create(OkHttpClient.Builder builder,
                           ServiceConfigurationProperties properties) {
    return HttpReactiveClient.builder()
        .clientBuilder(builder)
        .baseUrl(properties.searchEndpoint(SERVICE_NAME))
        .connectTimeout(Duration.ofMillis(300L))
        .readTimeout(Duration.ofMillis(1200L))
        .writeTimeout(Duration.ofMillis(700L))
        .buildProxy(InvoiceRepository.class);
  }
}