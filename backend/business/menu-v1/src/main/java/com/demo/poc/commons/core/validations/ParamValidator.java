package com.demo.poc.commons.core.validations;

import java.util.Map;

import com.demo.poc.commons.core.errors.exceptions.NoSuchParamMapperException;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.inject.Instance;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ParamValidator {

  private final Instance<ParamMapper> paramMappers;
  private final BodyValidator bodyValidator;

  public <T> Uni<T> validateAndGet(Map<String, String> paramsMap, Class<T> paramClass) {
    ParamMapper mapper = selectMapper(paramClass);
    @SuppressWarnings("unchecked")
    T params = (T) mapper.map(paramsMap);
    return bodyValidator.validateAndGet(params);
  }

  private ParamMapper selectMapper(Class<?> paramClass) {
    return paramMappers.stream()
        .filter(mapper -> mapper.supports(paramClass))
        .findFirst()
        .orElseThrow(() -> new NoSuchParamMapperException(paramClass));
  }
}