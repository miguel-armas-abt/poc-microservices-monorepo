package com.demo.bbq.business.invoice.domain.model.response;

import com.demo.bbq.business.invoice.infrastructure.documentation.data.MenuOrderExample;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrder implements Serializable {

  @Schema(example = MenuOrderExample.DESCRIPTION)
  private String description;

  @Schema(example = MenuOrderExample.PRICE)
  private BigDecimal price;

  @Schema(example = MenuOrderExample.QUANTITY)
  private Integer quantity;

  @Schema(example = MenuOrderExample.TOTAL)
  private BigDecimal total;
}
