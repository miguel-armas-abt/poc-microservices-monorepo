package com.demo.bbq.commons.core.config;

import com.demo.bbq.commons.core.interceptor.error.ErrorInterceptor;
import com.demo.bbq.commons.core.interceptor.restclient.request.RestClientRequestInterceptor;
import com.demo.bbq.commons.core.interceptor.restclient.response.RestClientResponseInterceptor;
import com.demo.bbq.commons.core.interceptor.restserver.RestServerInterceptor;
import com.demo.bbq.commons.core.logging.ThreadContextInjector;
import com.demo.bbq.commons.core.properties.ConfigurationBaseProperties;
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
  public ErrorInterceptor errorInterceptor(ThreadContextInjector threadContextInjector, ConfigurationBaseProperties properties) {
    return new ErrorInterceptor(threadContextInjector, properties);
  }

}