package com.demo.bbq.entrypoint.calculator.dto.request;

import java.io.Serializable;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO implements Serializable {

  private String productCode;
  private String description;

  @Positive
  private Integer quantity;
}
