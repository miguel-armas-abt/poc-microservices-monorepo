package com.demo.bbq.business.tableplacement.infrastructure.exception.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.WebFilter;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ApiExceptionHandler {

  private final ApiExceptionHandlerUtil apiExceptionHandlerUtil;

  @Bean
  public WebFilter exceptionHandlingFilter() {
    return ((exchange, chain) -> chain.filter(exchange)
        .onErrorResume(Exception.class, exception -> apiExceptionHandlerUtil.buildResponse(exception, exchange)));
  }
}
