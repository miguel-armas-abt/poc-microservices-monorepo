package com.demo.bbq.commons.cache.enums;

import com.demo.bbq.commons.properties.dto.cache.CacheTemplate;
import java.time.Duration;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CleaningFrequency {

  IMMEDIATE(0),
  MINUTELY(60),
  FIVE_MINUTES(300),
  HOURLY(3600),
  DAILY(86400),
  WEEKLY(604800);

  private final long timeToLiveInSeconds;

  public Duration getTimeToLive() {
    return Duration.ofSeconds(timeToLiveInSeconds);
  }

  public static Duration getTimeToLive(CacheTemplate cacheTemplate) {
    return Optional.ofNullable(cacheTemplate.getCleaningFrequency())
        .map(cleaningFrequency -> Duration.ofSeconds(cleaningFrequency.getTimeToLiveInSeconds()))
        .orElseGet(() -> Optional.ofNullable(Duration.ofSeconds(cacheTemplate.getCustomTtl()))
            .orElseGet(() -> Duration.ofSeconds(FIVE_MINUTES.getTimeToLiveInSeconds())));
  }
}