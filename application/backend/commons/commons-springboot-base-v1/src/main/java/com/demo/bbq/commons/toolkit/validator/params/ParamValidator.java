package com.demo.bbq.commons.toolkit.validator.params;

import com.demo.bbq.commons.toolkit.validator.body.BodyValidator;
import java.util.*;

import com.demo.bbq.commons.toolkit.validator.utils.ParamReflectiveMapper;
import lombok.RequiredArgsConstructor;

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