package com.demo.bbq.commons.core.config;

import com.demo.bbq.commons.core.interceptor.error.ErrorInterceptor;
import com.demo.bbq.commons.custom.properties.ApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@RequiredArgsConstructor
@Configuration
public class ResponseErrorFilter {

  private final ApplicationProperties properties;
  private final ErrorInterceptor errorInterceptor;

  @Bean
  public WebFilter exceptionHandlingFilter() {
    return (exchange, next) -> next.filter(exchange)
        .onErrorResume(Exception.class, exception -> errorInterceptor.handleException(properties, exception, exchange));
  }

}
