package com.demo.bbq.config.tracing.logging;

import com.demo.bbq.utils.properties.ConfigurationBaseProperties;
import com.demo.bbq.utils.tracing.logging.RestClientRequestLogger;
import com.demo.bbq.utils.tracing.logging.RestClientResponseLogger;
import com.demo.bbq.utils.tracing.logging.RestServerLogger;
import com.demo.bbq.utils.tracing.logging.obfuscation.header.strategy.HeaderObfuscationStrategy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class LoggingConfig {

  @Bean
  public ClientHttpRequestInterceptor restClientRequestLogger(ConfigurationBaseProperties properties,
                                                              List<HeaderObfuscationStrategy> headerObfuscationStrategies) {
    return new RestClientRequestLogger(properties, headerObfuscationStrategies);
  }

  @Bean
  public ClientHttpRequestInterceptor restClientResponseLogger(ConfigurationBaseProperties properties,
                                                               List<HeaderObfuscationStrategy> headerObfuscationStrategies) {
    return new RestClientResponseLogger(properties, headerObfuscationStrategies);
  }

  @Bean
  public RestServerLogger restServerLogger(ConfigurationBaseProperties properties,
                                             List<HeaderObfuscationStrategy> headerObfuscationStrategies) {
    return new RestServerLogger(properties, headerObfuscationStrategies);
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