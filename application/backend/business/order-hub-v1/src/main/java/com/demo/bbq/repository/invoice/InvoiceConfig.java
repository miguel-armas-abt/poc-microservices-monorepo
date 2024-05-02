package com.demo.bbq.repository.invoice;

import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.restclient.retrofit.HttpReactiveClient;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class InvoiceConfig {

  public static final String INVOICE_SERVICE_NAME = "invoice-v1";

  @Bean(INVOICE_SERVICE_NAME)
  InvoiceRepository create(OkHttpClient.Builder builder,
                           ServiceConfigurationProperties properties) {
    return HttpReactiveClient.builder()
        .clientBuilder(builder)
        .baseUrl(properties.searchEndpoint(INVOICE_SERVICE_NAME))
        .connectTimeout(Duration.ofMillis(300L))
        .readTimeout(Duration.ofMillis(1200L))
        .writeTimeout(Duration.ofMillis(700L))
        .buildProxy(InvoiceRepository.class);
  }
}