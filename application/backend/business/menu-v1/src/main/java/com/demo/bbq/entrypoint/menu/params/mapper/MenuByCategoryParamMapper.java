package com.demo.bbq.entrypoint.menu.params.mapper;

import com.demo.bbq.commons.toolkit.validator.params.ParamMapper;
import com.demo.bbq.entrypoint.menu.params.pojo.MenuByCategoryParams;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class MenuByCategoryParamMapper implements ParamMapper<MenuByCategoryParams> {

  @Override
  public MenuByCategoryParams mapParameters(Map<String, String> parametersMap) {
    return MenuByCategoryParams.builder()
        .category(parametersMap.get("category"))
        .build();
  }

  @Override
  public boolean supports(Class<MenuByCategoryParams> paramsClass) {
    return paramsClass.isAssignableFrom(MenuByCategoryParams.class);
  }
}
