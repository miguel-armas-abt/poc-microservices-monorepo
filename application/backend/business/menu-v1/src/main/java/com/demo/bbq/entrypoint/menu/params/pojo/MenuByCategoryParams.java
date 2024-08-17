package com.demo.bbq.entrypoint.menu.params.pojo;

import static com.demo.bbq.entrypoint.menu.params.constant.ParameterConstants.CATEGORY_REGEX;

import com.demo.bbq.commons.toolkit.validator.params.DefaultParams;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Builder
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class MenuByCategoryParams extends DefaultParams {

  @Pattern(regexp = CATEGORY_REGEX)
  private String category;
}