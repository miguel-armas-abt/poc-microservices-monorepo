package com.demo.poc.entrypoint.menu.repository.product.wrapper.request;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

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
