package com.demo.poc.commons.core.restclient.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Map;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FormDataFiller {

  public static MultiValueMap<String, String> fillFormData(Map<String, String> providedParams,
                                                           Map<String, String> currentParams) {

    MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
    if (Objects.nonNull(currentParams)) {
      currentParams.forEach(params::add);
    }
    if (Objects.nonNull(providedParams)) {
      providedParams.forEach(params::set);
    }
    return params;
  }
}