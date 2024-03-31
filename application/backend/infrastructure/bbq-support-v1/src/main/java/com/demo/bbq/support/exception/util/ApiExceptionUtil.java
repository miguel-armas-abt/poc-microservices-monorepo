package com.demo.bbq.support.exception.util;

public class ApiExceptionUtil {
  private ApiExceptionUtil() {}

  public static String buildErrorCode(String serviceName, String errorCode) {
    return serviceName
        .concat(".")
        .concat(errorCode.substring(5));
  }
}