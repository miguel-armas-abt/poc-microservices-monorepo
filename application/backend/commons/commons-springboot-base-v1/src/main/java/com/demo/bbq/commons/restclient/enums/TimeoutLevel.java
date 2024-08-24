package com.demo.bbq.commons.restclient.enums;

import java.time.Duration;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TimeoutLevel {

  EXCELLENT(1000, 200, 1000),
  GOOD(1500, 200, 2000),
  POOR(2000, 200, 4000),
  BAD(2500, 200, 8000);

  private final long connectionTimeoutMillis;
  private final long writeTimeoutMillis;
  private final long readTimeoutMillis;

  public Duration getConnectionTimeoutDuration() {
    return Duration.ofMillis(connectionTimeoutMillis);
  }

  public Duration getWriteTimeoutDuration() {
    return Duration.ofMillis(writeTimeoutMillis);
  }

  public Duration getReadTimeoutDuration() {
    return Duration.ofMillis(readTimeoutMillis);
  }
}