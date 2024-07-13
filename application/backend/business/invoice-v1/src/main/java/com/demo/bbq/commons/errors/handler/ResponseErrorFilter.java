package com.demo.bbq.commons.errors.handler;

import com.demo.bbq.commons.properties.ApplicationProperties;
import com.demo.bbq.commons.errors.handler.response.ResponseErrorHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@RequiredArgsConstructor
@Configuration
public class ResponseErrorFilter {

  private final ApplicationProperties properties;
  private final ResponseErrorHandler responseErrorHandler;

  @Bean
  public WebFilter exceptionHandlingFilter() {
    return (exchange, next) -> next.filter(exchange)
        .onErrorResume(Exception.class, exception -> responseErrorHandler.handleException(properties, exception, exchange));
  }

}
