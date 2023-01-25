package com.demo.bbq.business.invoice.infrastructure.repository.restclient.dto.menuoption;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOptionDto implements Serializable {

  private Long id;

  private String description;

  private String category;

  private BigDecimal price;

  private boolean active;

}
