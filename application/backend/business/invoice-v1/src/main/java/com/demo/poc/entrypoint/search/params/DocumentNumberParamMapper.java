package com.demo.poc.entrypoint.search.params;

import java.util.Map;

import com.demo.poc.commons.core.validations.ParamMapper;

import org.springframework.stereotype.Component;

@Component
public class DocumentNumberParamMapper implements ParamMapper {

  @Override
  public Object map(Map<String, String> params) {
    return DocumentNumberParam.builder()
        .documentNumber(params.get("documentNumber"))
        .documentType(params.get("documentType"))
        .build();
  }

  @Override
  public boolean supports(Class<?> paramClass) {
    return DocumentNumberParam.class.isAssignableFrom(paramClass);
  }
}
