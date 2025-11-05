package com.demo.service.entrypoint.menu.params;

import java.util.HashMap;
import java.util.Map;

import com.demo.commons.validations.ParamMapper;

import org.springframework.stereotype.Component;

@Component
public class CategoryParamMapper implements ParamMapper<CategoryParam> {

  private static final String CATEGORY = "category";

  @Override
  public Map.Entry<CategoryParam, Map<String, String>> map(Map<String, String> params) {
    CategoryParam categoryParam = CategoryParam.builder()
        .category(params.get(CATEGORY))
        .build();

    Map<String, String> categoryParamMap = new HashMap<>();
    categoryParamMap.put(CATEGORY, categoryParam.getCategory());

    return Map.entry(categoryParam, categoryParamMap);
  }

  @Override
  public boolean supports(Class<?> paramClass) {
    return CategoryParam.class.isAssignableFrom(paramClass);
  }
}
