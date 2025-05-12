package com.demo.poc.commons.core.restclient.utils;

import com.demo.poc.commons.core.tracing.enums.TraceParamType;
import jakarta.ws.rs.core.MultivaluedMap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ParameterMapFiller {

  public static Consumer<Map<String, String>> addProvidedParams(Map<String, String> providedParams) {
    return params -> Optional.ofNullable(providedParams).ifPresent(params::putAll);
  }

  public static Consumer<Map<String, String>> addGeneratedParams(Map<String, String> generatedParams) {
    return params -> Optional.ofNullable(generatedParams).ifPresent(headersMap -> headersMap
        .forEach((headerKey, traceParamType) -> params.put(headerKey, TraceParamType.valueOf(traceParamType).getParamGenerator().generateParam())));
  }

  public static Consumer<Map<String, String>> addForwardedParams(Map<String, String> forwardedParams,
                                                                 MultivaluedMap<String, String> currentParams) {
    return params -> Optional.ofNullable(forwardedParams).ifPresent(paramMap -> paramMap
        .forEach((paramKey, paramName) -> setForwardedIfPresent(params, currentParams, paramKey, paramName)));
  }

  private static void setForwardedIfPresent(Map<String, String> forwardedParams,
                                            MultivaluedMap<String, String> currentParams,
                                            String paramKey,
                                            String paramName) {
    Optional<String> currentParam = Optional.ofNullable(currentParams.getFirst(paramName));
    currentParam.ifPresent(paramValue -> forwardedParams.put(paramKey, paramValue));
  }
}
