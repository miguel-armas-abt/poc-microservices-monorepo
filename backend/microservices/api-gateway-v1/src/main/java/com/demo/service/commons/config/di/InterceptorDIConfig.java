package com.demo.service.commons.config.di;

import com.demo.commons.errors.selector.ResponseErrorSelector;
import com.demo.commons.interceptor.error.ErrorInterceptor;
import com.demo.commons.interceptor.restclient.request.RestClientRequestInterceptor;
import com.demo.commons.interceptor.restclient.response.RestClientResponseInterceptor;
import com.demo.commons.interceptor.restserver.RestServerInterceptor;
import com.demo.commons.logging.ErrorThreadContextInjector;
import com.demo.commons.logging.ThreadContextInjector;
import com.demo.commons.serialization.ByteSerializer;
import com.demo.service.commons.properties.ApplicationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.server.WebFilter;

@Configuration
public class InterceptorDIConfig {

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
