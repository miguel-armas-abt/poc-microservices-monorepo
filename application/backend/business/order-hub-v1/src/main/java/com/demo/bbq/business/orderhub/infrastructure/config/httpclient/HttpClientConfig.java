package com.demo.bbq.business.orderhub.infrastructure.config.httpclient;

import com.demo.bbq.business.orderhub.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.business.orderhub.infrastructure.config.intercept.ExchangeInterceptor;
import com.demo.bbq.utils.errors.external.RestClientErrorService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class HttpClientConfig {

  @Bean
  OkHttpClient.Builder client(List<RestClientErrorService> services,
                              ServiceConfigurationProperties properties) {

    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
    return new OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .addInterceptor(new ExchangeInterceptor(services, properties));
  }
}