package com.demo.poc.entrypoint.rules.base.dto.params;

import com.demo.poc.commons.core.validations.headers.DefaultHeaders;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StrategyHeader extends DefaultHeaders {

  @NotEmpty
  private String strategy;
}
