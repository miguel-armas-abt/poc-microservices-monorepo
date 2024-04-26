package com.demo.bbq.business.menu.application.dto.response;

import com.demo.bbq.business.menu.infrastructure.config.doc.DocumentationConfig.DocumentationExample;
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
public class MenuOptionResponseDTO implements Serializable {

  @Schema(example = DocumentationExample.PRODUCT_CODE)
  private String productCode;

  @Schema(example = DocumentationExample.UNIT_PRICE)
  private BigDecimal unitPrice;

  @Schema(example = DocumentationExample.DESCRIPTION)
  private String description;

  @Schema(example = DocumentationExample.CATEGORY_MAIN_DISH)
  private String category;

}
