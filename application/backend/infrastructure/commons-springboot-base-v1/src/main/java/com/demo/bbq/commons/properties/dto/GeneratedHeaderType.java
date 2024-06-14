package com.demo.bbq.commons.properties.dto;

import static com.demo.bbq.commons.toolkit.DateFormatterUtil.DEFAULT_DATE_PATTERN;
import static com.demo.bbq.commons.toolkit.DateFormatterUtil.getFormattedDate;
import static com.demo.bbq.commons.toolkit.TraceUtil.TRACE_ID_SIZE;
import static com.demo.bbq.commons.toolkit.TraceUtil.PARENT_ID_SIZE;
import static com.demo.bbq.commons.toolkit.GregorianCalendarUtil.getGregorianCalendar;
import static com.demo.bbq.commons.toolkit.TraceUtil.getTrace;

import com.demo.bbq.commons.properties.functions.HeaderGenerator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GeneratedHeaderType {

  TIMESTAMP("timestamp", () -> String.valueOf(getGregorianCalendar())),
  TIMESTAMP_V2("timestamp", () -> getFormattedDate.apply(DEFAULT_DATE_PATTERN)),
  TRACE_ID("traceId", () -> getTrace(TRACE_ID_SIZE)),
  PARENT_ID("parentId", () -> getTrace(PARENT_ID_SIZE));

  private final String key;
  private final HeaderGenerator headerGenerator;
}
