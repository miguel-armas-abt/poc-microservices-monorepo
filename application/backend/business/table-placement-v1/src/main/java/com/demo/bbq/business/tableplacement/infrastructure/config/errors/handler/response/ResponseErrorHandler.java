package com.demo.bbq.business.tableplacement.infrastructure.config.errors.handler.response;

import com.demo.bbq.business.tableplacement.application.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.errors.handler.response.ResponseErrorUtil;
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
