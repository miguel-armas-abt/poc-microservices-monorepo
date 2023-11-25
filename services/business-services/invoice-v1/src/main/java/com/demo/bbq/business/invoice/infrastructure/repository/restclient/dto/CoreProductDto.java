package com.demo.bbq.business.invoice.infrastructure.repository.restclient.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CoreProductDto implements Serializable {

  private String code;
  private BigDecimal unitPrice;
}
