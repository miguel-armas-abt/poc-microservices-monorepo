package com.demo.bbq.commons.toolkit.validator.params;

import com.demo.bbq.commons.toolkit.validator.utils.ParamName;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DummyQueryParam extends DefaultParams {

  @ParamName("userId")
  private String userId;

  private String productCode;
}
