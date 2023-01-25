package com.demo.bbq.business.menuoption.domain.model.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuOptionRequest implements Serializable {

  @Pattern(regexp = "^((\\w)+(\\s)?)*$", message = "Name has invalid format")
  @Size(min = 3, max = 300)
  @NotNull(message = "description cannot be null")
  private String description;

  @Pattern(regexp = "^(main-dish|drink|dessert)$", message = "Invalid menu option category")
  @NotNull(message = "category cannot be null")
  private String category;

  @NotNull(message = "price cannot be null")
  private BigDecimal price;

  @NotNull(message = "is active cannot be null")
  private boolean isActive;

}