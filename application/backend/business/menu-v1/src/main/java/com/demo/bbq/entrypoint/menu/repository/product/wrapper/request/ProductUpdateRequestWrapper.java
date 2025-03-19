package com.demo.bbq.entrypoint.menu.repository.product.wrapper.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequestWrapper implements Serializable {

  private BigDecimal unitPrice;
  private String scope;
}
