package com.demo.bbq.business.menuoption.domain.model.response;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOption implements Serializable {

  private Long id;

  private String description;

  private String category;

  private BigDecimal price;

  private boolean active;

}