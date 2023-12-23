package com.demo.bbq.business.menuoption.infrastructure.repository.restclient.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductSaveRequestDto implements Serializable {

  private String code;
  private BigDecimal unitPrice;
  private String scope;
}
