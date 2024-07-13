package com.demo.bbq.entrypoint.calculator.dto.request;

import java.io.Serializable;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO implements Serializable {

  private String productCode;
  private String description;
  private Integer quantity;

}
