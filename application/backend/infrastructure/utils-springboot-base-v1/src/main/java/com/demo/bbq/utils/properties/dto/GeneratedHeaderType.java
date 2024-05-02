package com.demo.bbq.utils.properties.dto;

import static com.demo.bbq.utils.toolkit.DateFormatterUtil.DEFAULT_DATE_PATTERN;
import static com.demo.bbq.utils.toolkit.DateFormatterUtil.getFormattedDate;
import static com.demo.bbq.utils.toolkit.TraceUtil.TRACE_ID_SIZE;
import static com.demo.bbq.utils.toolkit.TraceUtil.PARENT_ID_SIZE;
import static com.demo.bbq.utils.toolkit.GregorianCalendarUtil.getGregorianCalendar;
import static com.demo.bbq.utils.toolkit.TraceUtil.getTrace;

import com.demo.bbq.utils.properties.functions.HeaderGenerator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GeneratedHeaderType {

  TIMESTAMP(() -> String.valueOf(getGregorianCalendar())),
  TIMESTAMP_V2(() -> getFormattedDate.apply(DEFAULT_DATE_PATTERN)),
  TRACE_ID(() -> getTrace(TRACE_ID_SIZE)),
  PARENT_ID(() -> getTrace(PARENT_ID_SIZE));

  private final HeaderGenerator headerGenerator;
}
