package com.demo.bbq.commons.config;

import com.demo.bbq.commons.properties.ConfigurationBaseProperties;
import com.demo.bbq.commons.tracing.logging.error.ErrorLogger;
import com.demo.bbq.commons.tracing.logging.injector.ThreadContextInjector;
import com.demo.bbq.commons.tracing.logging.restclient.request.RestClientRequestLogger;
import com.demo.bbq.commons.tracing.logging.restclient.response.RestClientResponseLogger;
import com.demo.bbq.commons.tracing.logging.restserver.RestServerLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;

@Configuration
public class LoggingConfig {

  @Bean
  public ThreadContextInjector threadContextInjector(ConfigurationBaseProperties properties) {
    return new ThreadContextInjector(properties);
  }

  @Bean
  public ErrorLogger errorLogger(ThreadContextInjector threadContextInjector, ConfigurationBaseProperties properties) {
    return new ErrorLogger(threadContextInjector, properties);
  }

  @Bean
  public ClientHttpRequestInterceptor restClientRequestLogger(ThreadContextInjector threadContextInjector, ConfigurationBaseProperties properties) {
    return new RestClientRequestLogger(threadContextInjector, properties);
  }

  @Bean
  public ClientHttpRequestInterceptor restClientResponseLogger(ThreadContextInjector threadContextInjector, ConfigurationBaseProperties properties) {
    return new RestClientResponseLogger(threadContextInjector, properties);
  }

  @Bean
  public RestServerLogger restServerLogger(ThreadContextInjector threadContextInjector, ConfigurationBaseProperties properties) {
    return new RestServerLogger(threadContextInjector, properties);
  }

}