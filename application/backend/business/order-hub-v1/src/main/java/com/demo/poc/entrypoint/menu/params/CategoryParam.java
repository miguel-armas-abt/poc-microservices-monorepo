package com.demo.poc.entrypoint.menu.params;

import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.io.Serializable;

import static com.demo.poc.entrypoint.menu.constants.ParameterConstants.CATEGORY_REGEX;

@Builder
@Setter
@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class CategoryParam implements Serializable {

  @Pattern(regexp = CATEGORY_REGEX)
  private String category;
}