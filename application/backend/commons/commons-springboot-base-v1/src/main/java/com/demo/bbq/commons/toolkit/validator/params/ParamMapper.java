package com.demo.bbq.commons.toolkit.validator.params;

import java.util.Map;

public interface ParamMapper<T extends DefaultParams> {

  T mapParameters(Map<String, String> parametersMap);

  boolean supports(Class<T> paramsClass);
}
