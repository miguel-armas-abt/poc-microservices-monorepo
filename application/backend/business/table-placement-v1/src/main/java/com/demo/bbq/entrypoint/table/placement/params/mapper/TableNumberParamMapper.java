package com.demo.bbq.entrypoint.table.placement.params.mapper;

import static com.demo.bbq.entrypoint.table.placement.params.constants.ParamConstants.TABLE_NUMBER_PARAM;

import com.demo.bbq.commons.toolkit.validator.params.ParamMapper;
import com.demo.bbq.entrypoint.table.placement.params.pojo.TableNumberParam;
import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.Optional;

@Component
public class TableNumberParamMapper implements ParamMapper<TableNumberParam> {

  @Override
  public TableNumberParam mapParameters(Map<String, String> parametersMap) {
    return TableNumberParam.builder()
        .tableNumber(Optional.ofNullable(parametersMap.get(TABLE_NUMBER_PARAM)).map(Integer::parseInt).orElseGet(() -> null))
        .build();
  }

  @Override
  public boolean supports(Class<TableNumberParam> paramsClass) {
    return paramsClass.isAssignableFrom(TableNumberParam.class);
  }
}
