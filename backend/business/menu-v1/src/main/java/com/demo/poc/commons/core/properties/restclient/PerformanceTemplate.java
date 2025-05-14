package com.demo.poc.commons.core.properties.restclient;

import com.demo.poc.commons.core.restclient.enums.ConcurrencyLevel;
import com.demo.poc.commons.core.restclient.enums.TimeoutLevel;

public interface PerformanceTemplate {

  TimeoutLevel timeout();
  ConcurrencyLevel concurrency();
}