package com.demo.bbq.commons.logging.exclusion;

import io.micrometer.tracing.exporter.SpanExportingPredicate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Ignore URIs in Zipkin
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class URIExclusionUtil {

  public static SpanExportingPredicate noActuator() {
    return span -> span.getTags().get("uri") == null || !span.getTags().get("uri").startsWith("/actuator");
  }

}
