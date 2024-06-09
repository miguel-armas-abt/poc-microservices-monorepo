package com.demo.bbq.utils.toolkit;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Map;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HeaderPlanerUtil {

  public static <T,Q> Map<String, String> flatHeaders(MultivaluedMap<T, Q> headers) {
    return headers.entrySet()
        .stream()
        .collect(Collectors.toMap(entry -> (String) entry.getKey(), entry -> (String) entry.getValue().get(0)));
  }
}
