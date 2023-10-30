package com.demo.bbq.support.exception.documentation;

public class ApiExceptionJsonExample {

  private ApiExceptionJsonExample() {}

  public static final String BAD_REQUEST = "{\"type\":\"/errors/business-rules\",\"message\":\"The category is not defined\",\"errorCode\":\"01.0001\"}";

  public static final String NOT_FOUND = "{\"type\":\"/errors/no-data\",\"message\":\"The resource does not exist\",\"errorCode\":\"03.0001\"}";
}
