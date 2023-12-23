package com.demo.bbq.business.menu.domain.model.response;

import com.demo.bbq.business.menu.infrastructure.documentation.data.MenuOptionExample;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOption implements Serializable {

  @Schema(example = MenuOptionExample.PRODUCT_CODE)
  private String productCode;

  @Schema(example = MenuOptionExample.UNIT_PRICE)
  private BigDecimal unitPrice;

  @Schema(example = MenuOptionExample.DESCRIPTION)
  private String description;

  @Schema(example = MenuOptionExample.CATEGORY_MAIN_DISH)
  private String category;

}
