package com.demo.poc.commons.core.validations;

import com.demo.poc.commons.core.errors.exceptions.NoSuchParamMapperException;
import io.reactivex.rxjava3.core.Single;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ParamValidator {

  private final List<ParamMapper> paramMappers;
  private final BodyValidator bodyValidator;

  public <T> Single<T> validateAndGet(Map<String, String> paramsMap, Class<T> paramClass) {
    T params = (T) selectMapper(paramClass).map(paramsMap);
    return bodyValidator.validateAndGet(params);
  }

  private ParamMapper selectMapper(Class<?> paramClass) {
    return paramMappers.stream()
        .filter(mapper -> mapper.supports(paramClass))
        .findFirst()
        .orElseThrow(() -> new NoSuchParamMapperException(paramClass));
  }
}