package com.demo.bbq.commons.tracing.exclusion;

import io.micrometer.tracing.exporter.SpanExportingPredicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class URIExclusionUtil {

  public static SpanExportingPredicate noActuator() {
    return span -> span.getTags().get("uri") == null || !span.getTags().get("uri").startsWith("/actuator");
  }

}
