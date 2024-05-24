package com.demo.bbq.utils.interceptor.tracing;

import io.micrometer.tracing.exporter.SpanExportingPredicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TraceExclusionUtil {

  public static SpanExportingPredicate noActuator() {
    return span -> span.getTags().get("uri") == null || !span.getTags().get("uri").startsWith("/actuator");
  }

}
