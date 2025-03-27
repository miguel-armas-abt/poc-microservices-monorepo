package com.demo.poc.entrypoint.menu.repository.product.wrapper.response;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseWrapper implements Serializable {

  private String code;
  private BigDecimal unitPrice;
}
