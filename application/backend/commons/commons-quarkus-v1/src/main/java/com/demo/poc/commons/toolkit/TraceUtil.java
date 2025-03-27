package com.demo.poc.commons.toolkit;

import java.security.SecureRandom;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TraceUtil {

  public static final int PARENT_ID_SIZE = 16;
  public static final int TRACE_ID_SIZE = 32;

  public static String getTrace(int size) {
    StringBuilder traceState = new StringBuilder();
    SecureRandom random = new SecureRandom();
    byte[] randomBytes = new byte[(size / 2)];
    random.nextBytes(randomBytes);
    for (byte randomByte : randomBytes) {
      String hex = Integer.toHexString(randomByte + 128);
      if (hex.length() == 1) {
        hex = "0" + hex;
      }
      traceState.append(hex);
    }
    return traceState.toString();
  }
}
