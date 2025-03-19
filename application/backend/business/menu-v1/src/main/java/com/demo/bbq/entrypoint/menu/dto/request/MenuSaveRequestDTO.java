package com.demo.bbq.entrypoint.menu.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

import static com.demo.bbq.entrypoint.menu.constants.ParameterConstants.CATEGORY_REGEX;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuSaveRequestDTO implements Serializable {

  @NotEmpty
  private String productCode;

  @Size(min = 3, max = 300)
  @NotEmpty
  private String description;

  @Pattern(regexp = CATEGORY_REGEX)
  @NotEmpty
  private String category;

  @NotNull
  private BigDecimal unitPrice;
}