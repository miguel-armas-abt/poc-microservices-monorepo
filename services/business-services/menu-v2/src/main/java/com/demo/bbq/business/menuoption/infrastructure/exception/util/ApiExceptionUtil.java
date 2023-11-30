package com.demo.bbq.business.menuoption.infrastructure.exception.util;

import com.demo.bbq.business.menuoption.infrastructure.exception.catalog.ApiExceptionType;

public class ApiExceptionUtil {
  private ApiExceptionUtil() {}

  private static final String DOT = ".";

  public static String generateErrorCode(ApiExceptionType exceptionType, String serviceName, String errorCode) {
    return serviceName
        .concat(DOT)
        .concat(exceptionType.getCode())
        .concat(DOT)
        .concat(errorCode.substring(5));
  }
}