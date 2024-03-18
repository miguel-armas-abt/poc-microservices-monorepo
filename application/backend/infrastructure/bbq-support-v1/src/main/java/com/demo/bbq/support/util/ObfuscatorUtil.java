package com.demo.bbq.support.util;

public class ObfuscatorUtil {

  private ObfuscatorUtil() {}

  public static String partiallyObfuscate(String value) {
    int length = value.length();
    if (length > 6) {
      var start = value.substring(0, 3);
      var end = value.substring(length - 3);
      return start + "*" + end;
    }
    return value;
  }
}
