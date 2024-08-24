package com.demo.bbq.commons.properties.dto.restclient;

import com.demo.bbq.commons.restclient.enums.ConcurrencyLevel;
import com.demo.bbq.commons.restclient.enums.TimeoutLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PerformanceTemplate {

  private TimeoutLevel timeout;
  private ConcurrencyLevel concurrency;
}