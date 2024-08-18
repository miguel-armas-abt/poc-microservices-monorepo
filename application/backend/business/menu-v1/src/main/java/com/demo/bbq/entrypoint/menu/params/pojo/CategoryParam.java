package com.demo.bbq.entrypoint.menu.params.pojo;

import static com.demo.bbq.entrypoint.menu.params.constant.ParameterConstants.CATEGORY_REGEX;

import com.demo.bbq.commons.toolkit.validator.params.DefaultParams;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CategoryParam extends DefaultParams implements Serializable {

  @Pattern(regexp = CATEGORY_REGEX)
  private String category;
}