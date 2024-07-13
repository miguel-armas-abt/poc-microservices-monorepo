package com.demo.bbq.commons.toolkit.restclient.headers;

import static com.demo.bbq.commons.toolkit.restclient.headers.TimestampHeaderUtil.*;
import static com.demo.bbq.commons.toolkit.restclient.headers.TraceHeaderUtil.TRACE_ID_SIZE;
import static com.demo.bbq.commons.toolkit.restclient.headers.TraceHeaderUtil.PARENT_ID_SIZE;
import static com.demo.bbq.commons.toolkit.restclient.headers.TraceHeaderUtil.getTrace;

import com.demo.bbq.commons.properties.functions.HeaderGenerator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GeneratedHeaderType {

  TIMESTAMP("timestamp", () -> String.valueOf(getGregorianCalendar())),
  TIMESTAMP_V2("timestamp", () -> formatDate.apply(DEFAULT_DATE_PATTERN)),
  TRACE_ID("traceId", () -> getTrace(TRACE_ID_SIZE)),
  PARENT_ID("parentId", () -> getTrace(PARENT_ID_SIZE));

  private final String key;
  private final HeaderGenerator headerGenerator;
}
