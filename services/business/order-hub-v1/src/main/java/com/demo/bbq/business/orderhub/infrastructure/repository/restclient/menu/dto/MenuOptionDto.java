package com.demo.bbq.business.orderhub.infrastructure.repository.restclient.menu.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOptionDto implements Serializable {

  private String productCode;

  private String description;

  private String category;

  private BigDecimal unitPrice;

}
