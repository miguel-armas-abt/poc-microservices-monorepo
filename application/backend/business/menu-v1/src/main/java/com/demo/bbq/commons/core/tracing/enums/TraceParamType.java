package com.demo.bbq.commons.core.tracing.enums;

import com.demo.bbq.commons.core.tracing.utils.TraceParamGenerator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TraceParamType {

  TIMESTAMP_V2("timestamp", () -> TraceParamGenerator.formatDate.apply(TraceParamGenerator.DEFAULT_DATE_PATTERN)),
  TRACE_ID("trace-id", () -> TraceParamGenerator.getTrace(TraceParamGenerator.TRACE_ID_SIZE)),
  PARENT_ID("parent-id", () -> TraceParamGenerator.getTrace(TraceParamGenerator.PARENT_ID_SIZE));

  private final String key;
  private final ParamGenerator paramGenerator;

  @FunctionalInterface
  public interface ParamGenerator {
    String generateParam();
  }
}
