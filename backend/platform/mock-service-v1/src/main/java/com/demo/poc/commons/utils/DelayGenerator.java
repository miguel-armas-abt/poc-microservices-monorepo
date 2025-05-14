package com.demo.poc.commons.utils;

import java.util.concurrent.ThreadLocalRandom;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DelayGenerator {

  private static final double EXPONENTIAL_SCALE = 250;

  public static long generateRandomDelay() {
    return generateRandomDelayWithProbabilityDensity(EXPONENTIAL_SCALE);
  }

  public static long generateRandomDelayWithProbabilityDensity(double exponentialScale) {
    double u = ThreadLocalRandom.current().nextDouble();
    long delay = (long) (-exponentialScale * Math.log(1 - u));
    delay = Math.max(250, Math.min(2000, delay));
    return delay;
  }
}
