package com.demo.bbq.business.waiterorder.infrastructure.repository.restclient.invoice.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDto implements Serializable {

  private String description;
  private Integer quantity;
  private BigDecimal unitPrice;

}
