package com.demo.bbq.utils.properties.enums;

import static com.demo.bbq.utils.toolkit.DateFormatterUtil.DEFAULT_DATE_PATTERN;
import static com.demo.bbq.utils.toolkit.DateFormatterUtil.getFormattedDate;
import static com.demo.bbq.utils.toolkit.GregorianCalendarUtil.getGregorianCalendar;
import static com.demo.bbq.utils.toolkit.TraceUtil.*;

import com.demo.bbq.utils.properties.functions.HeaderGenerator;
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
