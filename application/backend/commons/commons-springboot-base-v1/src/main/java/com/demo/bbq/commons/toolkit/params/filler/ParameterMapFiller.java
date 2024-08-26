package com.demo.bbq.commons.toolkit.params.filler;

import com.demo.bbq.commons.toolkit.params.enums.GeneratedParamType;
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

  public static Consumer<Map<String, String>> addGeneratedParams(Map<String, GeneratedParamType> generatedParams) {
    return params -> Optional.ofNullable(generatedParams).ifPresent(paramMap -> paramMap
        .forEach((headerKey, generatedParamType) -> params.put(headerKey, generatedParamType.getParamGenerator().generateParam())));
  }

  public static Consumer<Map<String, String>> addForwardedParams(Map<String, String> forwardedParams,
                                                                 Map<String, String> currentParams) {
    return params -> Optional.ofNullable(forwardedParams).ifPresent(paramMap -> paramMap
        .forEach((paramKey, paramName) -> setForwardedIfPresent(params, currentParams, paramKey, paramName)));
  }

  private static void setForwardedIfPresent(Map<String, String> forwardedParams,
                                            Map<String, String> currentParams,
                                            String paramKey,
                                            String paramName) {
    Optional<String> currentParam = Optional.ofNullable(currentParams.get(paramName));
    currentParam.ifPresent(paramValue -> forwardedParams.put(paramKey, paramValue));
  }
}
