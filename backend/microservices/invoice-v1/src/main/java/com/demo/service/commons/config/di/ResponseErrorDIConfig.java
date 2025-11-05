package com.demo.service.commons.config.di;

import com.demo.commons.interceptor.error.ErrorInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@RequiredArgsConstructor
@Configuration
public class ResponseErrorDIConfig {

  private final ErrorInterceptor errorInterceptor;

  @Bean
  public WebFilter exceptionHandlingFilter() {
    return (exchange, next) -> next.filter(exchange)
        .onErrorResume(Exception.class, exception -> errorInterceptor.handleException(exception, exchange));
  }

}
