package com.demo.bbq.commons.toolkit.params.filler;

import static com.demo.bbq.commons.toolkit.params.filler.ParameterMapFiller.*;

import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FormDataFiller {

  public static Map<String, String> fillFormData(Map<String, String> providedParams,
                                                 Map<String, String> currentParams) {

    Map<String, String> params = new HashMap<>(currentParams);
    addProvidedParams(providedParams).accept(params);
    return params;
  }
}