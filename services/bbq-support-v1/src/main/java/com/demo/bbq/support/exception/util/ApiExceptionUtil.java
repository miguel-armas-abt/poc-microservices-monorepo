package com.demo.bbq.support.exception.util;

import com.demo.bbq.support.constant.CharacterConstant;
import com.demo.bbq.support.exception.catalog.ApiExceptionType;

public class ApiExceptionUtil {
  private ApiExceptionUtil() {}

  public static String generateErrorCode(ApiExceptionType exceptionType, String serviceName, String errorCode) {
    return serviceName
        .concat(CharacterConstant.DOT)
        .concat(exceptionType.getCode())
        .concat(CharacterConstant.DOT)
        .concat(errorCode.substring(5));
  }
}
