package com.demo.bbq.config.interceptor.tracing;

import com.demo.bbq.utils.interceptor.tracing.TraceExclusionUtil;
import io.micrometer.tracing.exporter.SpanExportingPredicate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TracingConfig {

  @Bean
  SpanExportingPredicate noActuator() {
    return TraceExclusionUtil.noActuator();
  }
}
