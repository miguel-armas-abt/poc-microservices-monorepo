package com.demo.poc.entrypoint.menu.repository.product.wrapper.request;

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
public class ProductSaveRequestWrapper implements Serializable {

  private String code;
  private BigDecimal unitPrice;
  private String scope;
}
