package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.invoice.dto.response;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Product implements Serializable {

  private String description;
  private Integer quantity;
  private BigDecimal unitPrice;
  private BigDecimal subtotal;

}
