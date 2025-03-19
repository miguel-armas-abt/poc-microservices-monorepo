package com.demo.bbq.commons.validations.params;

import com.demo.bbq.commons.validations.body.BodyValidator;
import com.demo.bbq.commons.validations.utils.ParamReflectiveMapper;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class ParamValidator {

  private final BodyValidator bodyValidator;

  public <T extends DefaultParams> T validateAndRetrieve(Map<String, String> params, Class<T> paramClass) {
    T parameters = ParamReflectiveMapper.mapParam(params, paramClass, false);
    bodyValidator.validate(parameters);
    return parameters;
  }

  public <T extends DefaultParams> void validate(Map<String, String> params, Class<T> paramClass) {
    validateAndRetrieve(params, paramClass);
  }
}