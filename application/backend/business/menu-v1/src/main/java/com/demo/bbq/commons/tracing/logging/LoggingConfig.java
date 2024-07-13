package com.demo.bbq.commons.tracing.logging;

import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoggingConfig {

  @Bean
  public ClientHttpRequestInterceptor restClientRequestLogger(ConfigurationBaseProperties properties) {
    return new RestClientRequestLogger(properties);
  }

  @Bean
  public ClientHttpRequestInterceptor restClientResponseLogger(ConfigurationBaseProperties properties) {
    return new RestClientResponseLogger(properties);
  }

  @Bean
  public RestServerLogger restServerLogger(ConfigurationBaseProperties properties) {
    return new RestServerLogger(properties);
  }

  @Configuration
  @RequiredArgsConstructor
  public static class RestServerLoggerConfig implements WebMvcConfigurer {

    private final RestServerLogger restServerLogger;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(restServerLogger);
    }
  }

}