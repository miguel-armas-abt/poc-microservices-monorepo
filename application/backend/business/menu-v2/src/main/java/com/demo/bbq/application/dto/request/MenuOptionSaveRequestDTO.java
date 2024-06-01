package com.demo.bbq.application.dto.request;

import com.demo.bbq.application.constant.MenuRegex;
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
public class MenuOptionSaveRequestDTO implements Serializable {

  @NotNull(message = "productCode cannot be null")
  private String productCode;

  @Size(min = 3, max = 300)
  @NotNull(message = "description cannot be null")
  private String description;

  @Pattern(regexp = MenuRegex.CATEGORY, message = "Invalid menu category")
  @NotNull(message = "category cannot be null")
  private String category;

  @NotNull(message = "unitPrice cannot be null")
  private BigDecimal unitPrice;
}
