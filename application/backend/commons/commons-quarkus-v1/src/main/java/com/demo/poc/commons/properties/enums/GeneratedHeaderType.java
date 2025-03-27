package com.demo.poc.commons.properties.enums;

import static com.demo.poc.commons.toolkit.DateFormatterUtil.DEFAULT_DATE_PATTERN;
import static com.demo.poc.commons.toolkit.DateFormatterUtil.getFormattedDate;
import static com.demo.poc.commons.toolkit.GregorianCalendarUtil.getGregorianCalendar;
import static com.demo.poc.commons.toolkit.TraceUtil.*;

import com.demo.poc.commons.properties.functions.HeaderGenerator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GeneratedHeaderType {

  TIMESTAMP("timestamp", () -> String.valueOf(getGregorianCalendar())),
  TIMESTAMP_V2("timestamp", () -> getFormattedDate.apply(DEFAULT_DATE_PATTERN)),
  TRACE_ID("trace-id", () -> getTrace(TRACE_ID_SIZE)),
  PARENT_ID("parent-id", () -> getTrace(PARENT_ID_SIZE));

  private final String key;
  private final HeaderGenerator headerGenerator;
}
