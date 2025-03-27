package com.demo.poc.entrypoint.menu.dto.params;

import com.demo.poc.commons.core.validations.params.DefaultParams;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;

import static com.demo.poc.entrypoint.menu.constants.ParameterConstants.CATEGORY_REGEX;

@Builder
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CategoryParam extends DefaultParams implements Serializable {

  @Pattern(regexp = CATEGORY_REGEX)
  private String category;
}