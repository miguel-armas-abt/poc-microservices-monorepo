package com.demo.poc.entrypoint.menu.params;

import java.util.Map;

import com.demo.poc.commons.core.validations.ParamMapper;

import org.springframework.stereotype.Component;

@Component
public class CategoryParamMapper implements ParamMapper {

  @Override
  public Object map(Map<String, String> params) {
    return CategoryParam.builder()
        .category(params.get("category"))
        .build();
  }

  @Override
  public boolean supports(Class<?> paramClass) {
    return CategoryParam.class.isAssignableFrom(paramClass);
  }
}
