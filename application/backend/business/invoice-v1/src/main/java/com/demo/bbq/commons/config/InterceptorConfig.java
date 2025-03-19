package com.demo.bbq.commons.config;

import com.demo.bbq.commons.interceptor.error.ErrorInterceptor;
import com.demo.bbq.commons.interceptor.restclient.request.RestClientRequestInterceptor;
import com.demo.bbq.commons.interceptor.restclient.response.RestClientResponseInterceptor;
import com.demo.bbq.commons.interceptor.restserver.RestServerInterceptor;
import com.demo.bbq.commons.logging.ThreadContextInjector;
import com.demo.bbq.commons.properties.base.ConfigurationBaseProperties;
import com.demo.bbq.commons.serialization.ByteSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.server.WebFilter;

@Configuration
public class InterceptorConfig {

  @Bean
  public ExchangeFilterFunction restClientRequestInterceptor(ThreadContextInjector threadContextInjector, ConfigurationBaseProperties properties) {
    return new RestClientRequestInterceptor(threadContextInjector, properties);
  }

  @Bean
  public ExchangeFilterFunction restClientResponseInterceptor(ThreadContextInjector threadContextInjector, ConfigurationBaseProperties properties) {
    return new RestClientResponseInterceptor(threadContextInjector, properties);
  }

  @Bean
  public WebFilter restServerInterceptor(ThreadContextInjector threadContextInjector, ConfigurationBaseProperties properties) {
    return new RestServerInterceptor(threadContextInjector, properties);
  }

  @Bean
  public ErrorInterceptor responseErrorHandler(ByteSerializer byteSerializer,
                                               ThreadContextInjector threadContextInjector,
                                               ConfigurationBaseProperties properties) {
    return new ErrorInterceptor(byteSerializer, threadContextInjector, properties);
  }
}
