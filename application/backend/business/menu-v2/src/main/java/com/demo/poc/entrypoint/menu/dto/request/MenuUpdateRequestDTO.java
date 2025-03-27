package com.demo.poc.entrypoint.menu.dto.request;

import static com.demo.poc.entrypoint.menu.constant.ConstraintConstant.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuUpdateRequestDTO implements Serializable {

  @Size(min = 3, max = 300)
  @NotBlank(message = MENU_DESCRIPTION_NOT_BLANK_MESSAGE)
  private String description;

  @Pattern(regexp = CATEGORY_REGEX, message = CATEGORY_INVALID_MESSAGE)
  @NotBlank(message = CATEGORY_NOT_BLANK_MESSAGE)
  private String category;

  @NotNull(message = UNIT_PRICE_NOT_NULL_MESSAGE)
  private BigDecimal unitPrice;
}