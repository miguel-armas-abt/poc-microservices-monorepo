package com.demo.bbq.entrypoint.calculator.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO implements Serializable {

  @NotNull
  private String productCode;

  @NotNull
  private String description;

  @Positive
  @NotNull
  private Integer quantity;

  @JsonIgnore
  private Double discount;

  @JsonIgnore
  private BigDecimal unitPrice;
}
