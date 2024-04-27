package com.demo.bbq.application.dto.request;

import com.demo.bbq.config.doc.DocumentationConfig.DocumentationExample;
import com.demo.bbq.application.constant.MenuRegex;
import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
public class MenuOptionSaveRequestDTO implements Serializable {

  @Schema(example = DocumentationExample.PRODUCT_CODE)
  @NotNull(message = "productCode cannot be null")
  private String productCode;

  @Schema(example = DocumentationExample.DESCRIPTION)
  @Size(min = 3, max = 300)
  @NotNull(message = "description cannot be null")
  private String description;

  @Schema(example = DocumentationExample.CATEGORY_MAIN_DISH)
  @Pattern(regexp = MenuRegex.CATEGORY, message = "Invalid menu option category")
  @NotNull(message = "category cannot be null")
  private String category;

  @Schema(example = DocumentationExample.UNIT_PRICE)
  @NotNull(message = "unitPrice cannot be null")
  private BigDecimal unitPrice;
}