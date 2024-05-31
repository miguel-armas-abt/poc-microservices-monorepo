package com.demo.bbq.config.tracing.logging;

import com.demo.bbq.config.properties.ServiceConfigurationProperties;
import com.demo.bbq.utils.tracing.logging.RestServerLoggerUtil;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.strategy.HeaderObfuscationStrategy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@Component
@RequiredArgsConstructor
public class RestServerLogger implements HandlerInterceptor {

  private final ServiceConfigurationProperties properties;
  private final List<HeaderObfuscationStrategy> headerObfuscationStrategies;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    RestServerLoggerUtil.decorateRequest(properties, headerObfuscationStrategies, request);
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    RestServerLoggerUtil.decorateResponse(properties, headerObfuscationStrategies, request, response);
  }

  @Configuration
  @RequiredArgsConstructor
  public static class RestServerLoggerConfig implements WebMvcConfigurer {

    private final RestServerLogger restServerLogger;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(restServerLogger);
    }
  }
}
