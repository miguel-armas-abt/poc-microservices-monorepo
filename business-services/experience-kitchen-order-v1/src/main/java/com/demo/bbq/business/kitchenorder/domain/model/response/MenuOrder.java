package com.demo.bbq.business.kitchenorder.domain.model.response;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuOrder implements Serializable {

  private Long id;

  private String description;

  private String category;

  private Integer quantity;

  private Boolean isCooking;
}
