package com.demo.poc.commons.core.config;

import com.demo.poc.commons.core.logging.exclusion.URIExclusionUtil;
import io.micrometer.tracing.exporter.SpanExportingPredicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TraceExclusionConfig {

  @Bean
  SpanExportingPredicate noActuator() {
    return URIExclusionUtil.noActuator();
  }
}
