package com.demo.bbq.commons.core.properties.restclient;

import com.demo.bbq.commons.core.restclient.enums.ConcurrencyLevel;
import com.demo.bbq.commons.core.restclient.enums.TimeoutLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerformanceTemplate {

  private TimeoutLevel timeout;
  private ConcurrencyLevel concurrency;
}