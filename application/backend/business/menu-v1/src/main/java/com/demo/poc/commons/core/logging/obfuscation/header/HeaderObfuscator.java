package com.demo.poc.commons.core.logging.obfuscation.header;

import com.demo.poc.commons.core.logging.obfuscation.constants.ObfuscationConstant;
import com.demo.poc.commons.core.properties.logging.ObfuscationTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


public class HeaderObfuscator {

  public static String process(ObfuscationTemplate obfuscation,
                               Map<String, String> headers) {

    return headers
        .entrySet()
        .stream()
        .map(entry -> obfuscateHeader(entry, obfuscation.getHeaders()))
        .collect(Collectors.joining(", "));
  }

  private static String obfuscateHeader(Map.Entry<String, String> header,
                                        Set<String> sensitiveHeaders) {
    String key = header.getKey();
    return !sensitiveHeaders.contains(key)
        ? key + "=" + header.getValue()
        : Optional.ofNullable(header.getValue())
        .map(value -> key + "=" + partiallyObfuscate(value))
        .orElse(key + "=" + ObfuscationConstant.OBFUSCATION_WARNING);
  }

  private static String partiallyObfuscate(String value) {
    return value.length() <= 6
        ? ObfuscationConstant.OBFUSCATION_MASK
        : value.substring(0, 3) + ObfuscationConstant.OBFUSCATION_MASK + value.substring(value.length() - 3);
  }
}