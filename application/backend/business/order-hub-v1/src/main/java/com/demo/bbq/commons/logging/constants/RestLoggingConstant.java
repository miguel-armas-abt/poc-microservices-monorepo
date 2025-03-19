package com.demo.bbq.commons.logging.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestLoggingConstant {

  public static final String METHOD = ".method";
  public static final String URI = ".uri";
  public static final String HEADERS = ".headers";
  public static final String BODY = ".body";
  public static final String STATUS = ".status";
}
