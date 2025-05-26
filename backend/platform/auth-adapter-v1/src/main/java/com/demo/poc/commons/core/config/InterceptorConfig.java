package com.demo.poc.commons.core.config;

import com.demo.poc.commons.core.errors.selector.ResponseErrorSelector;
import com.demo.poc.commons.core.interceptor.error.ErrorInterceptor;
import com.demo.poc.commons.core.interceptor.restclient.request.RestClientRequestInterceptor;
import com.demo.poc.commons.core.interceptor.restclient.response.RestClientResponseInterceptor;
import com.demo.poc.commons.core.interceptor.restserver.RestServerInterceptor;
import com.demo.poc.commons.core.logging.ErrorThreadContextInjector;
import com.demo.poc.commons.core.logging.ThreadContextInjector;
import com.demo.poc.commons.core.serialization.ByteSerializer;
import com.demo.poc.commons.custom.properties.ApplicationProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.server.WebFilter;

@Configuration
public class InterceptorConfig {

  @Bean
  public ExchangeFilterFunction restClientRequestInterceptor(ThreadContextInjector contextInjector,
                                                             ApplicationProperties properties) {
    return new RestClientRequestInterceptor(contextInjector, properties);
  }

  @Bean
  public ExchangeFilterFunction restClientResponseInterceptor(ThreadContextInjector contextInjector,
                                                              ApplicationProperties properties) {
    return new RestClientResponseInterceptor(contextInjector, properties);
  }

  @Bean
  public WebFilter restServerInterceptor(ThreadContextInjector contextInjector,
                                         ApplicationProperties properties) {
    return new RestServerInterceptor(contextInjector, properties);
  }

  @Bean
  public ErrorInterceptor responseErrorHandler(ByteSerializer byteSerializer,
                                               ErrorThreadContextInjector contextInjector,
                                               ApplicationProperties properties,
                                               ResponseErrorSelector responseErrorSelector) {
    return new ErrorInterceptor(byteSerializer, contextInjector, properties, responseErrorSelector);
  }
}
