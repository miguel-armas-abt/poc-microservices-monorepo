package com.demo.bbq.entrypoint.menu.params.mapper;

import static com.demo.bbq.entrypoint.menu.params.constant.ParameterConstants.CATEGORY_PARAM;

import com.demo.bbq.commons.toolkit.validator.params.ParamMapper;
import com.demo.bbq.entrypoint.menu.params.pojo.CategoryParam;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CategoryParamMapper implements ParamMapper<CategoryParam> {

  @Override
  public CategoryParam mapParameters(Map<String, String> parametersMap) {
    return CategoryParam.builder()
        .category(parametersMap.get(CATEGORY_PARAM))
        .build();
  }

  @Override
  public boolean supports(Class<CategoryParam> paramsClass) {
    return paramsClass.isAssignableFrom(CategoryParam.class);
  }
}
