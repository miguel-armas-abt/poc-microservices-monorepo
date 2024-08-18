package com.demo.bbq.entrypoint.menu.params.mapper;

import com.demo.bbq.commons.toolkit.validator.params.ParamMapper;
import com.demo.bbq.entrypoint.menu.params.pojo.CategoryParam;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class CategoryParamMapper implements ParamMapper<CategoryParam> {

  @Override
  public CategoryParam mapParameters(Map<String, String> parametersMap) {
    return CategoryParam.builder()
        .category(parametersMap.get("category"))
        .build();
  }

  @Override
  public boolean supports(Class<CategoryParam> paramsClass) {
    return paramsClass.isAssignableFrom(CategoryParam.class);
  }
}
