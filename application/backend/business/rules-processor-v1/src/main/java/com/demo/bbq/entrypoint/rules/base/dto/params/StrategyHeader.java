package com.demo.bbq.entrypoint.rules.base.dto.params;

import com.demo.bbq.commons.toolkit.validator.headers.DefaultHeaders;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StrategyHeader extends DefaultHeaders {

  @NotEmpty
  private String strategy;
}
