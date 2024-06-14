package com.demo.bbq.config.tracing.tracing;

import com.demo.bbq.commons.tracing.exclusion.URIExclusionUtil;
import io.micrometer.tracing.exporter.SpanExportingPredicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingExclusionConfig {

  @Bean
  SpanExportingPredicate noActuator() {
    return URIExclusionUtil.noActuator();
  }
}
