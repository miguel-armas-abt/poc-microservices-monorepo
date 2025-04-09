package com.demo.poc.commons.utils;

import static org.mockserver.model.Header.header;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.mockserver.model.Header;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeadersGenerator {

  private static final String TRACE_ID = "trace-id";
  private static final String COLON = "-";

  public static Header generateTraceId() {
    return header(TRACE_ID, UUID.randomUUID().toString().replaceAll(COLON, StringUtils.EMPTY).toLowerCase());
  }

  public static Header contentType(String mime) {
    return header("Content-Type", mime);
  }

}
