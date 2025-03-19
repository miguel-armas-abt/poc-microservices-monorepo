package com.demo.bbq.entrypoint.menu.dto.params;

import com.demo.bbq.commons.validations.params.DefaultParams;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;

import static com.demo.bbq.entrypoint.menu.constants.ParameterConstants.CATEGORY_REGEX;

@Builder
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CategoryParam extends DefaultParams implements Serializable {

  @Pattern(regexp = CATEGORY_REGEX)
  private String category;
}