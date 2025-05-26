package com.demo.poc.commons.core.properties.restclient;

import java.util.Map;

public interface RestClient {

  PerformanceTemplate performance();
  RequestTemplate request();
  Map<String, RestClientError> errors();
}
