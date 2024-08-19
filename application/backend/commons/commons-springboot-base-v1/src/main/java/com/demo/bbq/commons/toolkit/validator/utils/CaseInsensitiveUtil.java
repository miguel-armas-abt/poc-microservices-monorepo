package com.demo.bbq.commons.toolkit.validator.utils;

import java.util.Map;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CaseInsensitiveUtil {

  public static Map<String, String> toLowerCaseKeys(Map<String, String> paramMap) {
    return paramMap.entrySet().stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().toLowerCase(),
            Map.Entry::getValue
        ));
  }

  public static String getInsensitiveCaseParam(Map<String, String> paramMap, String paramKey) {
    return paramMap.get(paramKey.toLowerCase());
  }
}
