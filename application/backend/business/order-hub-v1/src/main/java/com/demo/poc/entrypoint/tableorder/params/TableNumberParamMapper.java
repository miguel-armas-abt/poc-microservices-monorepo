package com.demo.poc.entrypoint.tableorder.params;

import java.util.Map;

import com.demo.poc.commons.core.validations.ParamMapper;

import org.springframework.stereotype.Component;

@Component
public class TableNumberParamMapper implements ParamMapper {

  @Override
  public Object map(Map<String, String> params) {
    return TableNumberParam.builder()
        .tableNumber(Integer.parseInt(params.get("tableNumber")))
        .build();
  }

  @Override
  public boolean supports(Class<?> paramClass) {
    return TableNumberParam.class.isAssignableFrom(paramClass);
  }
}
