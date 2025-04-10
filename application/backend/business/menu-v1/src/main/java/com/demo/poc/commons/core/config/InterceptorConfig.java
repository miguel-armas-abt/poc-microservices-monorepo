package com.demo.poc.commons.core.config;

import com.demo.poc.commons.core.errors.selector.ResponseErrorSelector;
import com.demo.poc.commons.core.interceptor.error.ErrorInterceptor;
import com.demo.poc.commons.core.interceptor.restclient.request.RestClientRequestInterceptor;
import com.demo.poc.commons.core.interceptor.restclient.response.RestClientResponseInterceptor;
import com.demo.poc.commons.core.interceptor.restserver.RestServerInterceptor;
import com.demo.poc.commons.core.logging.ThreadContextInjector;
import com.demo.poc.commons.core.properties.ConfigurationBaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;

@Configuration
public class InterceptorConfig {

  @Bean
  public ClientHttpRequestInterceptor restClientRequestInterceptor(ThreadContextInjector threadContextInjector, ConfigurationBaseProperties properties) {
    return new RestClientRequestInterceptor(threadContextInjector, properties);
  }

  @Bean
  public ClientHttpRequestInterceptor restClientResponseInterceptor(ThreadContextInjector threadContextInjector, ConfigurationBaseProperties properties) {
    return new RestClientResponseInterceptor(threadContextInjector, properties);
  }

  @Bean
  public RestServerInterceptor restServerInterceptor(ThreadContextInjector threadContextInjector, ConfigurationBaseProperties properties) {
    return new RestServerInterceptor(threadContextInjector, properties);
  }

  @Bean
  public ErrorInterceptor errorInterceptor(ThreadContextInjector threadContextInjector,
                                           ConfigurationBaseProperties properties,
                                           ResponseErrorSelector responseErrorSelector) {
    return new ErrorInterceptor(threadContextInjector, properties, responseErrorSelector);
  }

}