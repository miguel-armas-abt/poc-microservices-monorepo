package com.demo.bbq.commons.core.config;

import com.demo.bbq.commons.core.logging.exclusion.URIExclusionUtil;
import io.micrometer.tracing.exporter.SpanExportingPredicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TraceExclusionConfig {

  @Bean
  public SpanExportingPredicate noActuator() {
    return URIExclusionUtil.noActuator();
  }
}
