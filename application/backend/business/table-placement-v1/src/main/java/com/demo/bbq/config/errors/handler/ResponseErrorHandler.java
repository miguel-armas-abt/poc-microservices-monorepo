package com.demo.bbq.config.errors.handler;

import com.demo.bbq.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.errors.handler.ResponseErrorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@RequiredArgsConstructor
@Configuration
public class ResponseErrorHandler {

  private final ServiceConfigurationProperties properties;

  @Bean
  public WebFilter exceptionHandlingFilter() {
    return (exchange, next) -> next.filter(exchange)
        .onErrorResume(Exception.class, exception -> ResponseErrorUtil.handleException(properties, exception, exchange));
  }

}
