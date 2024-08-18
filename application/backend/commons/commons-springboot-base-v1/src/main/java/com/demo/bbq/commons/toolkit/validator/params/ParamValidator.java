package com.demo.bbq.commons.toolkit.validator.params;

import com.demo.bbq.commons.errors.exceptions.SystemException;
import com.demo.bbq.commons.toolkit.validator.body.BodyValidator;
import java.util.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ParamValidator {

  private final List<ParamMapper> paramMappers;
  private final BodyValidator bodyValidator;

  public <T extends DefaultParams> T validateAndRetrieve(Map<String, String> paramsMap,
                                                         Class<T> paramClass) {
    T parameters = (T) selectMapper(paramClass).mapParameters(paramsMap);
    bodyValidator.validate(parameters);
    return parameters;
  }

  public <T extends DefaultParams> void validate(Map<String, String> paramsMap,
                                                 Class<T> paramClass) {
    validateAndRetrieve(paramsMap, paramClass);
  }

  private ParamMapper selectMapper(Class<? extends DefaultParams> paramClass) {
    return paramMappers
        .stream()
        .filter(mapper -> mapper.supports(paramClass))
        .findFirst()
        .orElseThrow(() -> new SystemException("NoSuchParamMapper"));
  }
}