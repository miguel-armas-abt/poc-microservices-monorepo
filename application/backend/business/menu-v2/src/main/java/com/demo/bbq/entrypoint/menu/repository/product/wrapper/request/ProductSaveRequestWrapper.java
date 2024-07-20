package com.demo.bbq.entrypoint.menu.repository.product.wrapper.request;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSaveRequestWrapper implements Serializable {

  private String code;
  private BigDecimal unitPrice;
  private String scope;
}
