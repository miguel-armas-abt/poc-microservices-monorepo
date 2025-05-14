package com.demo.poc.commons.core.properties.restclient;

public interface RestClientError {

  String customCode();
  String message();
  Integer httpCode();
}