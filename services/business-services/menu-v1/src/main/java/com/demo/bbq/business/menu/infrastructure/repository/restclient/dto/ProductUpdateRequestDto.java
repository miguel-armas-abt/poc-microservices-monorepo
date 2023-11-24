package com.demo.bbq.business.menu.infrastructure.repository.restclient.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductUpdateRequestDto implements Serializable {

  private BigDecimal unitPrice;
  private String scope;
}
