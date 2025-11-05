package com.demo.service.entrypoint.table.placement.params;

import java.util.HashMap;
import java.util.Map;

import com.demo.commons.validations.ParamMapper;
import org.springframework.stereotype.Component;

@Component
public class TableNumberParamMapper implements ParamMapper<TableNumberParam> {

  private static final String TABLE_NUMBER = "tableNumber";

  @Override
  public Map.Entry<TableNumberParam, Map<String, String>> map(Map<String, String> params) {
    TableNumberParam tableNumberParam = TableNumberParam.builder()
        .tableNumber(Integer.parseInt(params.get(TABLE_NUMBER)))
        .build();

    Map<String, String> paramMap = new HashMap<>();
    paramMap.put(TABLE_NUMBER, String.valueOf(tableNumberParam.getTableNumber()));

    return Map.entry(tableNumberParam, paramMap);
  }

  @Override
  public boolean supports(Class<?> paramClass) {
    return TableNumberParam.class.isAssignableFrom(paramClass);
  }
}
