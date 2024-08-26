package com.demo.bbq.commons.toolkit.validator.headers;

import com.demo.bbq.commons.toolkit.validator.body.BodyValidator;
import com.demo.bbq.commons.toolkit.validator.utils.ParamReflectiveMapper;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class HeaderValidator {

  private final BodyValidator bodyValidator;

  public <T extends DefaultHeaders> T validateAndRetrieve(Map<String, String> headers,
                                                          Class<T> headerClass) {
    T parameters = ParamReflectiveMapper.mapParam(headers, headerClass, true);
    bodyValidator.validate(parameters);
    return parameters;
  }

  public <T extends DefaultHeaders> void validate(Map<String, String> headers, Class<T> headerClass) {
    validateAndRetrieve(headers, headerClass);
  }
}