package com.demo.bbq.entrypoint.menu.dto.request;

import static com.demo.bbq.entrypoint.menu.constants.ParameterConstants.CATEGORY_REGEX;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuUpdateRequestDTO implements Serializable {

  @Size(min = 3, max = 300)
  @NotEmpty
  private String description;

  @Pattern(regexp = CATEGORY_REGEX)
  @NotEmpty
  private String category;

  @NotNull
  private BigDecimal unitPrice;
}