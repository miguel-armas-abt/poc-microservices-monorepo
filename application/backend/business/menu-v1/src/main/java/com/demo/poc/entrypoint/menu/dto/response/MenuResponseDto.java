package com.demo.poc.entrypoint.menu.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class MenuResponseDto implements Serializable {

  private String productCode;

  private BigDecimal unitPrice;

  private String description;

  private String category;

}
