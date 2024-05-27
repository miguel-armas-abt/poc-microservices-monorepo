package com.demo.bbq.config.tracing.exclusion;

import com.demo.bbq.utils.tracing.exclusion.URIExclusionUtil;
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
