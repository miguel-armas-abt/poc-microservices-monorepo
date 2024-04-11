package com.demo.bbq.business.menuoption.domain.repository.product.wrapper.response;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseWrapper implements Serializable {

  private String code;
  private BigDecimal unitPrice;
}
