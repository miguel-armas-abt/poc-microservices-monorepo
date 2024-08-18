package com.demo.bbq.commons.toolkit.params.enums;

import static com.demo.bbq.commons.toolkit.params.utils.TimestampParamUtil.*;
import static com.demo.bbq.commons.toolkit.params.utils.TraceParamUtil.TRACE_ID_SIZE;
import static com.demo.bbq.commons.toolkit.params.utils.TraceParamUtil.PARENT_ID_SIZE;
import static com.demo.bbq.commons.toolkit.params.utils.TraceParamUtil.getTrace;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GeneratedParamType {

  TIMESTAMP("timestamp", () -> String.valueOf(getGregorianCalendar())),
  TIMESTAMP_V2("timestamp", () -> formatDate.apply(DEFAULT_DATE_PATTERN)),
  TRACE_ID("traceId", () -> getTrace(TRACE_ID_SIZE)),
  PARENT_ID("parentId", () -> getTrace(PARENT_ID_SIZE));

  private final String key;
  private final ParamGenerator paramGenerator;

  @FunctionalInterface
  public interface ParamGenerator {
    String generateHeader();
  }
}
